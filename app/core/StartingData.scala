package core

import play.api.libs.json.{Writes, JsArray, JsValue}
import play.api.libs.json.Json._
import core.Tileset._

class StartingData(val current: Player, val units: List[Unit], val tileset: Tileset, val height: Int, val width: Int,
                   val terrain: List[(Int, Int, Tile)], val vision: List[(Int, Int, Int)]) {

  implicit def tuple3Writes = new Writes[(Int, Int, AnyVal)] {
    def writes(o: (Int, Int, AnyVal)): JsValue = JsArray(Seq(toJson(o._1), toJson(o._2), toJson(o._3)))
  }

  implicit def enumWrites[T <: Enumeration#Value] = new Writes[T] {
    def writes(o: T): JsValue = toJson(o.toString)
  }

  def asJson: JsValue = {
    toJson(
      Map(
         "player" -> playerJson,
         "units" -> JsArray(units map unitJson),
         "tileset" -> toJson(tileset),
         "height" -> toJson(height),
         "width" -> toJson(width),
         "terrain" -> toJson(terrain),
         "vision" -> toJson(vision)
      )
    )
  }

  def unitJson(unit: Unit): JsValue = {
    // todo add action, target, direction
    toJson(
      Map(
        "x" -> toJson(unit.x),
        "y" -> toJson(unit.y),
        "playerNum" -> toJson(unit.player.number),
        "race" -> toJson(unit.player.race),
        "name" -> toJson(unit.uname)
    ))
  }

  def playerJson: JsValue = {
    toJson(Map(
      "number" -> toJson(current.number),
      "race" -> toJson(current.race),
      "gold" -> toJson(current.startResources._1),
      "lumber" -> toJson(current.startResources._2),
      "oil" -> toJson(current.startResources._3),
      "startX" -> toJson(current.startPos._1),
      "startY" -> toJson(current.startPos._2)
    ))
  }
}
