package controllers.json

import controllers.{ActionEvents, ClInitOk, WsInitOk, WebClientAction}
import game.Order
import game.json.ModelReads
import play.api.libs.json._

trait ApplicationReads extends ModelReads {
  implicit val tuple2reads = new Reads[(Int, Order)] {
    def reads(json: JsValue): JsResult[(Int, Order)] =
      JsSuccess((Integer.parseInt(Json.fromJson[String](json \ "unit").get), Json.fromJson[Order](json).get))
  }

  implicit val actionEventsReads = Json.reads[ActionEvents]

  implicit val webClientActionReads: Reads[_ >: WebClientAction] = new Reads[_ >: WebClientAction] {
    override def reads(json: JsValue): JsResult[_ >: WebClientAction] = (json \ "type").as[String] match {
      case "WebSocketInitOk" => JsSuccess(WsInitOk)
      case "ClientInitOk" => JsSuccess(ClInitOk)
      case "ActionEvents" => actionEventsReads.reads(json)
      case _ => JsError(s"Cannot parse json: $json")
    }
  }
}
