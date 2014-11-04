package game

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

import akka.actor._
import controllers.Tileset
import format.pud.Pud
import game.ControlledPlayerActor.{ClientInitOk, WebSocketInitOk}
import game.GameActor._
import game.PlayerActor.{DoAction, Init, InitOk}
import game.ai.{DefaultAi, NeutralAi}
import game.world.{Terrain, UpdateData, World}
import play.Logger
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue

object GameActor {
  case object NewGame
  case class GameCreated(game: Game)
  case class Error(message: String)

  case class PlayerWebSocketInitOk(playerId: Int, channel: Channel[JsValue])

  case class PlayerClientInitOk(playerId: Int)

  case object Update
  case object PrintTicks
}

class GameActor(map: Pud, tileset: Tileset.Value, peasantOnly: Boolean, playerSettings: List[(Int, Race, Control)]) extends Actor {
  val game = new Game(map, tileset, peasantOnly, playerSettings)
  val playerActors = game.players map { p => p.control match {
    case HumanControl => context.actorOf(Props(new ControlledPlayerActor(p)))
    case DefaultAiControl => context.actorOf(Props(new PlayerActor(p, new DefaultAi(p))))
    case NeutralAiControl => context.actorOf(Props(new PlayerActor(p, new NeutralAi(p))))
  }}

  val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

  def receive = newGame

  def awaitPlayers(playerSet: Set[ActorRef], newGameCreator: ActorRef): Receive = {
    case InitOk => if (playerSet.size == 1 && playerSet.contains(sender)) {
      newGameCreator ! GameCreated

      val fullUpdateData = world.init(pud, settings)

      scheduler.scheduleAtFixedRate(new Runnable {
        def run() {
          context.self ! Update
        }
      }, 1000, 100 / 2, TimeUnit.MILLISECONDS)

      players.foreach(p => p._1 ! PlayerActor.Update(fullUpdateData(p._2)))

      context.become(gameCycle)
    } else context.become(awaitPlayers(playerSet - sender, newGameCreator))

    case PlayerWebSocketInitOk(playerId, channel) => players.find(_._2 == playerId).foreach(_._1 ! WebSocketInitOk(channel))

    case PlayerClientInitOk(playerId) => players.find(_._2 == playerId).foreach(_._1 ! ClientInitOk)
  }

  def gameCycle: Receive = {
    case DoAction(actions) =>
    // validate orders
    // todo change order to new only after current AtomicAction is complete
    // construct actions
      // todo validate
      Logger.debug(actions.toString())
      actions foreach { p =>
        val u = world.units.find(_.id == p._1).get
        u.atomicAction = u.atomicAction ++ p._2.decompose(world, u)

        Logger.debug("Atomic actions now: " + u.atomicAction)
      }

    case Update => {
      ticks = ticks + 1
      val ud: Map[Int, UpdateData] = world.spentTick()
      ud.foreach {
        case (player, updateData) => players.find(_._2 == player).foreach(_._1 ! PlayerActor.Update(updateData))
      }
    }

    case PrintTicks => Logger.info(s"Ticks = $ticks")

    case PlayerWebSocketInitOk(playerId, channel) => players.find(_._2 == playerId).foreach(_._1 ! WebSocketInitOk(channel))
    case PlayerClientInitOk(playerId) => players.find(_._2 == playerId).foreach(_._1 ! ClientInitOk)
  }

  def newGame: Receive = {
    case NewGame =>
      playerActors.foreach(_ ! Init)

      context.become(awaitPlayers(playerActors.toSet, sender))
  }
}