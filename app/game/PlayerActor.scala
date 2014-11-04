package game

import akka.actor.Actor
import game.PlayerActor.{Init, InitOk, Update}
import game.ai.Ai
import game.world.{Player, UpdateData}

object PlayerActor {
  case object Init
  case object InitOk

  case class DoAction(actions: List[(Int, Order)])
  case class DoActionError(message: String)

  case class Update(updateData: UpdateData)
}

class PlayerActor(player: Player, ai: Ai) extends Actor {
  override def receive: Receive = {
    // todo fix
    case Init =>
      context.become(gameLoop(ai))
      sender ! InitOk
    case _ => Unit
  }

  def gameLoop(ai: Ai): Receive = {
    case Update(updateData: UpdateData) => ai.update(updateData)
  }
}
