package game

import akka.actor.Actor
import game.ControlledPlayerActor.{ClientInitOk, WebSocketInitOk}
import game.PlayerActor.{Init, InitOk, Update}
import game.world.Player
import models.unit.json.UnitCharacteristicWrites
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json._

object ControlledPlayerActor {
  case class WebSocketInitOk(channel: Channel[JsValue])

  case object ClientInitOk
}

class ControlledPlayerActor(player: Player) extends Actor {

  def receive: Receive = {
    case Init =>
      context.become(awaitWebSocketInitialization)
  }

  def awaitWebSocketInitialization: Receive = {
    case WebSocketInitOk(channel) =>
      channel.push(
        Json.obj(
          "playerNum" -> player.pudNumber,
          "race" -> player.race,
          "startPosX" -> player.startPos._1,
          "startPosY" -> player.startPos._2,
          "unitTypes" -> Json.toJson(player.unitTypes.toMap)))

      context.become(awaitClientInitialization(channel))
  }

  def awaitClientInitialization(channel: Channel[JsValue]): Receive = {
    case ClientInitOk =>
      context.parent ! InitOk
      context.become(gameCycle(channel))
  }

  def gameCycle(channel: Channel[JsValue]): Receive = {
    case Update(updateData) =>
//      Logger.debug(updateData.addedTerrain.toString())
      channel.push(Json.toJson(updateData))
  }

  implicit val raceWrites = new Writes[Race] {
    def writes(o: Race): JsValue = o match {
      case Human => JsString("human")
      case Orc => JsString("orc")
      case Neutral => JsString("neutral")
    }
  }

  implicit val ucWrites = new UnitCharacteristicWrites
}
