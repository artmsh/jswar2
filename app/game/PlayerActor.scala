package game

import akka.actor.Actor
import game.PlayerActor._
import game.ai.Ai
import game.unit.{TerrainAdd, ResourcesChange, UnitAdd, Change}
import game.world.{TileVisibility, Terrain, Player, UpdateData}

object PlayerActor {
  case object Init
  case object InitOk
  case object InitFailed

  case class MakeOrders(player: Player, actions: List[(Int, Order)])
  case class DoActionError(message: String)

  case class UpdateFirstTime(terrain: Game#Terrain)
  case class Update(changes: List[Change])
}

class PlayerActor(player: Player, ai: Ai) extends Actor {
  override def receive: Receive = {
    case Init =>
      ai.init(player.race, player.unitTypes, player.mapWidth, player.mapHeight, player.startPos, player.pudNumber)
      context.become(gameLoop(ai))
      sender ! InitOk
    case _ => Unit
  }

  def gameLoop(ai: Ai): Receive = {
    case Update(changes: List[Change]) =>
      val orders: List[(Int, Order)] = ai.update(changes)
      sender ! MakeOrders(player, orders)

    case UpdateFirstTime(terrain) =>
      val changes: List[Change] =
        List(ResourcesChange(player.gold, player.lumber, player.oil)) ++
        (player.units map { UnitAdd }) ++
        (player.seenPositions map { v: TileVisibility => TerrainAdd(terrain(v.y)(v.x), v) })

      val orders: List[(Int, Order)] = ai.update(changes)
      sender ! MakeOrders(player, orders)
  }
}
