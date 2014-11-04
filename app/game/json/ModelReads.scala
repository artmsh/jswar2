package game.json

import game.{Move, Order}
import play.api.libs.json.{Json, JsResult, JsValue, Reads}

trait ModelReads {
  implicit def orderReads = new Reads[Order] {
    def reads(json: JsValue): JsResult[Order] = (json \ "name").as[String] match {
      case "move" => Json.reads[Move].reads(json)
    }
  }
}
