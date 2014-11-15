package game

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

import akka.actor._
import controllers.Tileset
import format.pud.Pud
import game.ControlledPlayerActor.{ClientInitOk, WebSocketInitOk}
import game.GameActor.Update
import game.GameActor._
import game.PlayerActor._
import game.ai.{DefaultAi, NeutralAi}
import game.unit.Change
import game.world.{Player, UpdateData}
import play.Logger
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue
import utils.PausableThreadPoolExecutor

object GameActor {
  case object NewGame
  case class GameCreated(game: Game)
  case class Error(message: String)

  case object Update
  case object PrintTicks
}

class GameActor(gameId: Int, map: Pud, tileset: Tileset.Value, peasantOnly: Boolean, playerSettings: List[(Int, Race, Control)]) extends Actor {
  val game = new Game(gameId, map, tileset, peasantOnly, playerSettings)
  var playerActors = game.players map { p =>
    val name = s"player${p.pudNumber}"
    p.control match {
      case HumanControl => context.actorOf(Props(new ControlledPlayerActor(p)), name)
      case DefaultAiControl => context.actorOf(Props(new PlayerActor(p, new DefaultAi(p))), name)
      case NeutralAiControl => context.actorOf(Props(new PlayerActor(p, new NeutralAi(p))), name)
    }
  }

  val scheduler: PausableThreadPoolExecutor = new PausableThreadPoolExecutor

  def receive = newGame

  def newGame: Receive = {
    case NewGame =>
      playerActors.foreach(_ ! Init)

      context.become(awaitPlayers(playerActors.toSet, sender))
  }

  def awaitPlayers(playerSet: Set[ActorRef], newGameCreator: ActorRef): Receive = {
    case InitOk =>
      handleInitResponse(playerSet, newGameCreator, sender)

    case InitFailed =>
      playerActors -= sender
      handleInitResponse(playerSet, newGameCreator, sender)

//    case PlayerWebSocketInitOk(playerId, channel) => players.find(_._2 == playerId).foreach(_._1 ! WebSocketInitOk(channel))
//
//    case PlayerClientInitOk(playerId) => players.find(_._2 == playerId).foreach(_._1 ! ClientInitOk)
  }

  private def handleInitResponse(playerSet: Set[ActorRef], newGameCreator: ActorRef, sender: ActorRef) {
    if (playerSet.size == 1 && playerSet.contains(sender)) {
      newGameCreator ! GameCreated

      scheduler.scheduleAtFixedRate(new Runnable {
        def run() {
          context.self ! Update
        }
      }, 1000, 100 / 1, TimeUnit.MILLISECONDS)

      playerActors.foreach(pa => pa ! PlayerActor.UpdateFirstTime)

      context.become(gameCycle(Map()))
    } else context.become(awaitPlayers(playerSet - sender, newGameCreator))
  }

  def gameCycle(pendingOrders: Map[Player, List[(Int, Order)]]): Receive = {
    case MakeOrders(player, actions) =>
      // validate orders
      // todo validate
      context.become(gameCycle(pendingOrders + (player -> actions)))

    case Update =>
      val changes: List[Change] = game.executeStep(pendingOrders)
      playerActors.foreach(pa => pa ! PlayerActor.Update(changes))
      context.become(gameCycle(Map()))

//    case PlayerWebSocketInitOk(playerId, channel) => players.find(_._2 == playerId).foreach(_._1 ! WebSocketInitOk(channel))
//    case PlayerClientInitOk(playerId) => players.find(_._2 == playerId).foreach(_._1 ! ClientInitOk)
  }
}