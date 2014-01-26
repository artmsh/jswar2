package game

import akka.actor._
import game.GameActor.{Update, GameCreated, NewGame}
import format.pud.{PudCodec, Pud}
import game.PlayerActor.{DoAction, Init}
import play.api.libs.json.JsValue
import play.api.libs.iteratee.Concurrent.Channel
import concurrent.Future
import world.{PlayerStats, Terrain, World}
import play.libs.Akka
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._

object GameActor {
  case class NewGame(map: Pud, settings: GameSettings)
  case object GameCreated
  case class Error(message: String)

  case object Update
}

class GameActor(channel: Future[Channel[JsValue]]) extends Actor {
  var players = Map[ActorRef, Int]()
  var world = new World(Map(), Vector(), new Terrain(Vector(), 0, 0))

  def update {

  }

  def receive: Receive = {
    case NewGame(pud, settings) =>
      settings.playerSettings.foreach(p =>
        if (p._1 != settings.controlledPlayerNo)
          players += context.actorOf(Props(new PlayerActor(p._1))) -> p._1
        else
          players += context.actorOf(Props(new ControlledPlayerActor(p._1, channel))) -> p._1
      )

      val unitTypes = if (pud._pud.udta._2.isDefaultData == 1) unit.defaults else pud.unitCharacteristics
      players foreach(p => p._1 ! Init(unitTypes, pud.startingPos(p._2), settings.playerSettings(p._2).race))
      // todo wait for initialization

      val stats = players.values map {
        num => (num -> new PlayerStats(num, pud.players(num),
          settings.playerSettings(num).race, pud.startingRes(num), pud.startingPos(num)))
      }

      world.playerStats = world.playerStats ++ stats

      world.units = world.units ++ pud._pud.unit._2.units
        .filter(!_.isStartLocation)
        .filter(u => settings.playerSettings.contains(u.player))
        .map(u => u match {
        case PudCodec.Unit(_,_,_,15,_) => unit.Unit(u, Neutral, unitTypes)
        case u: PudCodec.Unit => unit.Unit(u, settings.playerSettings(u.player).race, unitTypes)
      })

      world.terrain = new Terrain(Vector.tabulate(pud.mapSizeY, pud.mapSizeX)
        ((row, column) => pud.tiles(row * pud.mapSizeX + column)), pud.mapSizeX, pud.mapSizeY)

      world.unitsOnMap = world._unitsOnMap

      sender ! GameCreated

      Akka.system.scheduler.schedule(
        1 second,
        (1f / 30) second,
        context.self,
        Update
      )

      players.foreach(p => p._1 ! PlayerActor.Update(world.updateDataFull(p._2)))

    case DoAction(actions) =>
      // validate orders
      // todo change order to new only after current AtomicAction is complete
      // construct actions

    case Update => update
  }
}