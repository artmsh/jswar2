package game

import akka.actor.Actor
import models.unit.UnitCharacteristic
import world.UpdateData

object PlayerActor {
  case class Init(unitTypes: Vector[(String, UnitCharacteristic)], startPos: (Int, Int), race: Race)

  case class DoAction(actions: List[(Int, Order)])
  case class DoActionError(message: String)

  case class Update(updateData: UpdateData)
}

class PlayerActor(playerNum: Int) extends Actor {
  override def receive: Receive = {
    // todo fix
    case _ => Unit
  }
}
