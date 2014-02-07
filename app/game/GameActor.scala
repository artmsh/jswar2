package game

import akka.actor._
import game.GameActor.{PlayerWebSocketInitOk, Update, GameCreated, NewGame}
import format.pud.Pud
import game.PlayerActor.{InitOk, DoAction, Init}
import play.api.libs.json.JsValue
import play.api.libs.iteratee.Concurrent.Channel
import world.{Terrain, World}
import play.libs.Akka
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import game.ControlledPlayerActor.WebSocketInitOk

object GameActor {
  case class NewGame(map: Pud, settings: GameSettings)
  case object GameCreated
  case class Error(message: String)

  case class PlayerWebSocketInitOk(playerId: Int, channel: Channel[JsValue])

  case object Update
}

class GameActor() extends Actor {
  var players = Map[ActorRef, Int]()
  var world = new World(Map(), Vector(), new Terrain(Vector(), 0, 0))

  def receive = newGame

  def awaitPlayers(playerSet: Set[ActorRef], newGameCreator: ActorRef, pud: Pud, settings: GameSettings): Receive = {
    case InitOk => if (playerSet.size == 1 && playerSet.contains(sender)) {
      newGameCreator ! GameCreated

      world.init(pud, settings)

      Akka.system.scheduler.schedule(
        1 second,
        (1f / 30) second,
        context.self,
        Update
      )

      players.foreach(p => p._1 ! PlayerActor.Update(world.updateDataFull(p._2)))

      context.become(gameCycle)
    } else context.become(awaitPlayers(playerSet - sender, newGameCreator, pud, settings))

    case PlayerWebSocketInitOk(playerId, channel) => players.find(_._2 == playerId).foreach(_._1 ! WebSocketInitOk(channel))
  }

  def gameCycle: Receive = {
    case DoAction(actions) =>
    // validate orders
    // todo change order to new only after current AtomicAction is complete
    // construct actions

    case Update => {
      world.spentTick()
    }
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