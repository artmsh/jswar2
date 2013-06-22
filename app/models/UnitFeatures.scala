package models

import play.api.libs.json.{JsObject, JsValue, Writes}

class UnitFeatures {

}

object UnitFeatures {
  implicit val unitFeaturesWrites = new Writes[UnitFeatures]{
    def writes(o: UnitFeatures): JsValue = JsObject(Nil)
  }
}