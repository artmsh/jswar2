package core

import play.api.libs.json.{Json, Writes, JsArray, JsValue}
import play.api.libs.json.Json._
import core.Tileset._

class StartingData(val current: Player, val units: List[Unit], val tileset: Tileset, val height: Int, val width: Int,
                   val terrain: List[(Int, Int, Tile)], val vision: List[(Int, Int, Int)]) {

  implicit def tuple3Writes[T <: AnyVal] = new Writes[(Int, Int, T)] {
    def writes(o: (Int, Int, T)): JsValue = JsArray(Seq(toJson(o._1), toJson(o._2), anyvalWrites.writes(o._3)))
  }

  def anyvalWrites[T <: AnyVal] = new Writes[T] {
    def writes(o: T): JsValue = toJson(if (o.isInstanceOf[Tile]) o.asInstanceOf[Tile].tile else o.asInstanceOf[Int])
  }

  implicit def enumWrites[T <: Enumeration#Value] = new Writes[T] {
    def writes(o: T): JsValue = toJson(o.toString)
  }

  def asJson: JsValue = Json.obj(
     "player" -> playerJson,
     "units" -> (units map unitJson),
     "tileset" -> tileset,
     "height" -> height,
     "width" -> width,
     "terrain" -> terrain,
     "vision" -> vision
  )

  def unitJson(unit: Unit): JsValue = Json.obj(
    // todo add action, target, direction
    "x" -> unit.x,
    "y" -> unit.y,
    "playerNum" -> unit.player.number,
    "race" -> unit.player.race,
    "name" -> unit.uname
  )

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
