package controllers

import java.io.File

import akka.actor._
import controllers.json.ApplicationReads
import game.ControlledPlayerActor.MakeOrders
import game._
import play.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.libs.concurrent.Akka
import play.api.libs.iteratee.{Concurrent, Iteratee}
import play.api.libs.json._
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import se.radley.plugin.enumeration.form._

import scala.concurrent.duration._

object Application extends Controller with ApplicationReads {
  val games: Games = new Games()

  def pudsWithDescription = games.puds.toSeq map { p => (p._1, p._2.description) }
  def jsonPlayerSlots = Json.toJson(games.puds map { p => (p._1, p._2.players map { _.num }) }).toString()
  def gamesList: List[(Int, List[Int])] = (games.games zip games.games.indices) map { indexedGame => (indexedGame._2, indexedGame._1._1.players.map(_.pudNumber)) }

  def index = Action {
    Ok(views.html.games(gamesList, newGameForm, pudsWithDescription, jsonPlayerSlots))
  }

  val singlePlayerForm = Form(
    mapping(
      "race" -> enum(controllers.Race),
      "resources" -> enum(Resources),
      "units" -> enum(Units),
      "opponents" -> enum(Opponents),
      "tileset" -> enum(Tileset),
      "mapFileName" -> nonEmptyText.verifying("Map not found", new File(_).exists())
    )(SinglePlayerSetting.apply)(SinglePlayerSetting.unapply)
  )

  val newGameForm = Form(
    tuple(
      "pudFileName" -> text,
      "tileset" -> enum(Tileset),
      "peasantOnly" -> boolean
    )
  )

//  def singlePlayerGame = Action { implicit request =>
//    singlePlayerForm.bindFromRequest.fold(
//      formWithErrors => BadRequest(views.html.newSinglePlayer(formWithErrors)),
//      value => {
////        Pud(value.mapFileName).fold(
////          e => NotFound(e),
////          pud => Ok(views.html.game(new UnitFeatures)(value)(pud))
////        )
//        Ok()
//      }
//    )
//  }

  def createGame = Action { implicit request =>
    newGameForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.games(gamesList, formWithErrors, pudsWithDescription, jsonPlayerSlots)),
      value => {
        val (pudFileName, tileset, peasantOnly) = value
        val playerSettings = Map(0 -> PlayerSettings(Orc, HumanControl), 6 -> PlayerSettings(Orc, DefaultAiControl),
          15 -> PlayerSettings(Orc, DefaultAiControl))
        val gameSettings = GameSettings(pudFileName, tileset, 0, playerSettings, peasantOnly)

        games.createNewGame(gameSettings, {
          Redirect(routes.Application.index)
        })

        Redirect(routes.Application.index)
      }
    )
  }

  def game(gameId: Int, playerId: Int) = Action { implicit request =>
    val g = games.getGame(gameId)

    if (g.isDefined && playerId < g.get.players.size)
        Ok(views.html.game(gameId, playerId, g.get.tileset, g.get.map))
    else NotFound
  }

  def incomingWsHandler(js: JsValue, channel: Concurrent.Channel[JsValue], gameId: Int, playerId: Int, playerActor: ActorRef) = {
    Json.fromJson[WebClientAction](js) match {
      case JsSuccess(WebSocketInitOk, _) => playerActor ! ControlledPlayerActor.WebSocketInitOk(channel)
      case JsSuccess(ClientInitOk, _) => playerActor ! ControlledPlayerActor.ClientInitOk
      case JsSuccess(ActionEvents(actionEvents), _) => playerActor ! MakeOrders(actionEvents)
      case _ => Logger.error(s"Error in json: $js")
    }
  }

  def ws(gameId: Int, playerId: Int) = WebSocket.using[JsValue] { request =>
    val proxyActor = Akka.system.actorOf(Props(new PlayerProxy))
    Akka.system.actorSelection(s"/user/game$gameId/player$playerId").tell(Identify, proxyActor)

    val (enumerator, channel) = Concurrent.broadcast[JsValue]
    val in = Iteratee.foreach[JsValue](event => incomingWsHandler(event, channel, gameId, playerId, proxyActor))

    (in, enumerator)
  }

  class PlayerProxy extends Actor {
    def receive = {
      case ActorIdentity(_, Some(ref)) =>
        context.become(active(ref))
      case ActorIdentity(_, None) =>
        context.stop(self)
    }

    def active(playerActor: ActorRef): Actor.Receive = {
      case ControlledPlayerActor.WebSocketInitOk(channel) => playerActor ! ControlledPlayerActor.WebSocketInitOk(channel)
      case ControlledPlayerActor.ClientInitOk => playerActor ! ControlledPlayerActor.ClientInitOk
      case MakeOrders(orders) => playerActor ! MakeOrders(orders)
    }
  }
}