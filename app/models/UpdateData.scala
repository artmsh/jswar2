package models

import play.api.libs.json.{Json, JsObject}

class UpdateData {
  def asJson: JsObject = {
    Json.obj()
  }
}
