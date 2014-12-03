package game.json

import game.world.{TileVisibility, Tile, Player}
import game.{Neutral, Orc, Human, Race}
import game.unit._
import models.unit.json.UnitCharacteristicWrites
import play.api.libs.json._

trait ModelWrites {
  implicit val raceWrites = new Writes[Race] {
    def writes(o: Race): JsValue = o match {
      case Human => JsString("human")
      case Orc => JsString("orc")
      case Neutral => JsString("neutral")
    }
  }

  implicit val ucWrites = new UnitCharacteristicWrites

  implicit val unitWrites = new Writes[Unit] {
    override def writes(o: Unit): JsValue = JsObject(List(
      "id" -> JsNumber(o.id),
      "player" -> JsNumber(o.player.pudNumber),
      "name" -> JsString(o.name),
      "x" -> JsNumber(o.x),
      "y" -> JsNumber(o.y),
      "data" -> JsNumber(o.data),
      "hp" -> JsNumber(o.hp),
      "armor" -> JsNumber(o.armor),
      "sightRange" -> JsNumber(o.sightRange),
      "atomicActions" -> Json.toJson(o.atomicAction)
    ))
  }

  def changeObj(obj: String, fields: Seq[(String, JsValue)]): JsValue =
    JsObject(("type" -> JsString(obj)) +: fields)

  implicit val changeWrites = new Writes[Change] {
    override def writes(o: Change): JsValue = o match {
      case UnitAdd(newUnit) => changeObj("UnitAdd", List("newUnit" -> Json.toJson(newUnit)))
      case UnitPositionChange(unit: Unit, newPosition: (Int, Int)) =>
        changeObj("UnitPositionChange", List("unit" -> Json.toJson(unit.id), "newPosition" -> Json.toJson(newPosition)))
      case UnitActionsChange(unit: game.unit.Unit, actions: Unit#ActionsType) =>
        changeObj("UnitActionsChange", List("unit" -> Json.toJson(unit.id), "actions" -> Json.toJson(actions)))
      case UnitHeadActionChange(unit: Unit, newAction: AtomicAction) =>
        changeObj("UnitHeadActionChange", List("unit" -> Json.toJson(unit.id), "newAction" -> Json.toJson(newAction)))
      case UnitOccupyCell(unit: game.unit.Unit, position: (Int, Int)) =>
        changeObj("UnitOccupyCell", List("unit" -> Json.toJson(unit.id), "position" -> Json.toJson(position)))
      case ResourcesChange(player: Player, goldDelta: Int, lumberDelta: Int, oilDelta: Int) =>
        changeObj("ResourcesChange", List("goldDelta" -> Json.toJson(goldDelta),
          "lumberDelta" -> Json.toJson(lumberDelta), "oilDelta" -> Json.toJson(oilDelta)))
      case TerrainAdd(player: Player, tile: Tile, visibility: TileVisibility) =>
        changeObj("TerrainAdd", List("tile" -> Json.toJson(tile),
          "visibility" -> Json.toJson(visibility)))
      case TerrainVisibilityChange(player: Player, newVisibility: TileVisibility) =>
        changeObj("TerrainVisibilityChange", List("newVisibility" -> Json.toJson(newVisibility)))
    }
  }
}
