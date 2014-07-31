package models.unit.json

import models.unit._
import play.api.libs.json.{Json, JsValue, Writes}
import play.api.libs.json.Json.JsValueWrapper
import models.unit.BasicParams
import utils.UsefulWrites
import format.pud.json.PudWrites

class UnitCharacteristicWrites extends Writes[UnitCharacteristic] with UsefulWrites with PudWrites {
  implicit val basicWrites = Json.writes[BasicParams]
  implicit val attackWrites = Json.writes[AttackParams]
  implicit val uiWrites = Json.writes[UiParams]
  implicit val buildWrites = Json.writes[BuildParams]

  override def writes(o: UnitCharacteristic): JsValue = {
    val map = Map[String, JsValueWrapper](
       "basic" -> Json.toJson(o.pudUc.basic),
       "attack" -> Json.toJson(o.pudUc.attack),
       "ui" -> Json.toJson(o.pudUc.ui),
       "build" -> Json.toJson(o.pudUc.build),
       "moveSpeed" -> o.moveSpeed
    )

    Json.obj(map.toSeq:_*)
  }
}