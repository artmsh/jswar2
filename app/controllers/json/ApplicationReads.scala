package controllers.json

import game.Order
import game.json.ModelReads
import play.api.libs.json._

trait ApplicationReads extends ModelReads {
  implicit val tuple2reads = new Reads[(Int, Order)] {
    def reads(json: JsValue): JsResult[(Int, Order)] =
      JsSuccess((Integer.parseInt(Json.fromJson[String](json \ "unit").get), Json.fromJson[Order](json).get))
  }

}
