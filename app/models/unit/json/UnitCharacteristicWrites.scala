package models.unit.json

import models.unit.UnitCharacteristic
import play.api.libs.json.{JsValue, Writes}

class UnitCharacteristicWrites extends Writes[UnitCharacteristic] {
  override def writes(o: UnitCharacteristic): JsValue = ???
}
