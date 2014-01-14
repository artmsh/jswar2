package game

import akka.actor.Actor
import scala.collection.Map
import models.unit.UnitCharacteristic

object PlayerActor {
  case class Init(unitTypes: Map[String, UnitCharacteristic])
}

class PlayerActor extends Actor {
  override def receive: Receive = {
    // todo fix
    case _ => Unit
  }
}
