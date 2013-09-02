package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

// todo remove 'case' modifier
case class ClientEvent(dummy: Int) {

}

object ClientEvent {
  implicit val eventsFormat = Json.format[ClientEvent]
}