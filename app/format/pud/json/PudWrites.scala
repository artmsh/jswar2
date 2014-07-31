package format.pud.json

import play.api.libs.json._
import format.pud._
import play.api.libs.json.JsString

trait PudWrites {
  implicit object KindWrites extends Writes[Kind] {
    def writes(o: Kind): JsValue = o match {
      case Land => JsString("land")
      case Fly => JsString("fly")
      case Naval => JsString("naval")
    }
  }

  implicit val canTargetWrites = new Writes[CanTarget] {
    def writes(o: CanTarget): JsValue = Json.toJson(o.b)
  }

  implicit val missileWrites = new Writes[Missile] {
    def writes(o: Missile): JsValue = Json.toJson(o.getClass.getSimpleName)
  }

  implicit val mouseBtnAction = new Writes[MouseBtnAction] {
    def writes(o: MouseBtnAction): JsValue = Json.toJson(o.getClass.getSimpleName)
  }

}
