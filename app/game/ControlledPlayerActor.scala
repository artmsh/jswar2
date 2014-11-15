package game

import akka.actor.Actor
import game.ControlledPlayerActor.{ClientInitOk, WebSocketInitOk}
import game.PlayerActor.{UpdateFirstTime, Init, InitOk, Update}
import game.json.ModelWrites
import game.unit.{TerrainAdd, UnitAdd, ResourcesChange, Change}
import game.world.{TileVisibility, Player}
import models.unit.json.UnitCharacteristicWrites
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json._

object ControlledPlayerActor {
  case class WebSocketInitOk(channel: Channel[JsValue])

  case object ClientInitOk

  case class MakeOrders(orders: List[(Int, Order)])
}

class ControlledPlayerActor(val player: Player) extends AbstractPlayerActor with ModelWrites {
  def receive: Receive = {
    case Init =>
      context.become(awaitWebSocketInitialization)
  }

  def awaitWebSocketInitialization: Receive = {
    case WebSocketInitOk(channel) =>
      channel.push(
        Json.obj(
          "race" -> player.race,
          "unitTypes" -> Json.toJson(player.unitTypes.toMap),
          "mapWidth" -> player.mapWidth,
          "mapHeight" -> player.mapHeight,
          "startPosX" -> player.startPos.x,
          "startPosY" -> player.startPos.y,
          "playerNum" -> player.pudNumber
      ))

      context.become(awaitClientInitialization(channel))
  }

  def awaitClientInitialization(channel: Channel[JsValue]): Receive = {
    case ClientInitOk =>
      context.parent ! InitOk
      context.become(gameCycle(channel))
  }

  def gameCycle(channel: Channel[JsValue]): Receive = {
    case UpdateFirstTime(terrain) =>
      channel.push(Json.toJson(updateForFirstTime(terrain)))

    case Update(updateData) =>
      channel.push(Json.toJson(updateData))
  }
}
