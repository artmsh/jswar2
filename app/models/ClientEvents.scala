package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

// todo remove 'case' modifier
case class ClientEvents(dummy: Int) {

}

object ClientEvents {
  implicit val eventsFormat = Json.format[ClientEvents]
}