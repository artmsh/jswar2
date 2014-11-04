package controllers

import java.io.File

import akka.actor.ActorRef
import controllers.json.ApplicationReads
import game.GameActor.{PlayerClientInitOk, PlayerWebSocketInitOk}
import game.PlayerActor.DoAction
import game._
import play.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.libs.iteratee.{Concurrent, Iteratee}
import play.api.libs.json._
import play.api.mvc._
import se.radley.plugin.enumeration.form._

import scala.concurrent.duration._

object Application extends Controller with ApplicationReads {
  implicit val timeout = akka.util.Timeout(5 second)

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

  def ws(gameId: Int, playerId: Int) = WebSocket.using[JsValue] { request =>
    val (enumerator, channel) = Concurrent.broadcast[JsValue]

    val in = Iteratee.foreach[JsValue](event => {

      Logger.debug(s"Received json event: $event")

      val gameActor: ActorRef = games.games(gameId)._2
      (event \ "type").as[String] match {
        case "WebSocketInitOk" => gameActor ! PlayerWebSocketInitOk(playerId, channel)
        case "ClientInitOk" => gameActor ! PlayerClientInitOk(playerId)
        case t: String => {
          Logger.debug(t + " " + event \ "actionEvents")
          try {
            val result: JsResult[List[(Int, Order)]] = Json.fromJson[List[(Int, Order)]](event \ "actionEvents")

            Logger.debug(result.toString)

            result.fold[Unit](err => {
              Logger.debug("error: " + err.toString())
              err.foreach(p => Logger.error(p._1 + " " + p._2))
            },
              obj => {
                Logger.debug("obj:" + obj.toString())
                gameActor ! DoAction(obj)
              }
            )
          } catch {
            case e: Exception => Logger.error(e.getMessage, e)
          }
        }
      }
    })

    (in, enumerator)
  }
}