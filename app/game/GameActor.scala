package game

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

import akka.actor._
import format.pud.Pud
import game.ControlledPlayerActor.{ClientInitOk, WebSocketInitOk}
import game.GameActor._
import game.PlayerActor.{DoAction, Init, InitOk}
import game.world.{Terrain, UpdateData, World}
import play.Logger
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue

object GameActor {
  case class NewGame(map: Pud, settings: GameSettings)
  case object GameCreated
  case class Error(message: String)

  case class PlayerWebSocketInitOk(playerId: Int, channel: Channel[JsValue])

  case class PlayerClientInitOk(playerId: Int)

  case object Update
  case object PrintTicks
}

class GameActor() extends Actor {
  var players = Map[ActorRef, Int]()
  var world = new World(Map(), Vector(), new Terrain(Vector(), 0, 0))
  var ticks: Int = 0

  val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

  def receive = newGame

  def awaitPlayers(playerSet: Set[ActorRef], newGameCreator: ActorRef, pud: Pud, settings: GameSettings): Receive = {
    case InitOk => if (playerSet.size == 1 && playerSet.contains(sender)) {
      newGameCreator ! GameCreated

      val fullUpdateData = world.init(pud, settings)

//      Akka.system.scheduler.schedule(
//        1 second,
//        (1000f / 30) milliseconds,
//        context.self,
//        Update
//      )

      scheduler.scheduleAtFixedRate(new Runnable {
        def run() {
          context.self ! Update
        }
      }, 1000, 100 / 2, TimeUnit.MILLISECONDS)

//      Akka.system.scheduler.schedule(
//        1 second,
//        1 second,
//        context.self,
//        PrintTicks
//      )

      players.foreach(p => p._1 ! PlayerActor.Update(fullUpdateData(p._2)))

      context.become(gameCycle)
    } else context.become(awaitPlayers(playerSet - sender, newGameCreator, pud, settings))

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
    case NewGame(pud, settings) =>
      settings.playerSettings.foreach(p =>
        if (p._1 != settings.controlledPlayerNo)
          players += context.actorOf(Props(new PlayerActor(p._1))) -> p._1
        else
          players += context.actorOf(Props(new ControlledPlayerActor(p._1))) -> p._1
      )

      players foreach(p => p._1 ! Init(pud.unitTypes, pud.startingPos(p._2), settings.playerSettings(p._2).race))

      context.become(awaitPlayers(players.keySet, sender, pud, settings))
  }
}