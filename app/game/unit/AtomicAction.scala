package game.unit

import game.world.{UpdateUnitData, World}
import play.api.libs.json._

trait AtomicAction {
  val ticksLeft: Int
  val unit: Unit
  def spentTick(world: World): (List[AtomicAction], UpdateUnitData)
}

case class Still(unit: Unit) extends AtomicAction {
  val ticksLeft = 1

  def spentTick(world: World): (List[AtomicAction], UpdateUnitData) = (List(), UpdateUnitData(Map(), Map(), List()))
}

/* precompute A* if not precomputed and if we reach non-empty field - we compute it again */
case class Move(x: Int, y: Int, unit: Unit, ticksLeft: Int, ticksOverall: Int) extends AtomicAction {
  require(ticksLeft >= 0)

  def spentTick(world: World): (List[AtomicAction], UpdateUnitData) = {
    if (ticksLeft == ticksOverall) {
      world.unitsOnMap(y)(x) match {
        case Some(_) => (List(Still(unit), this), UpdateUnitData(Map(), Map(), List()))
        case None =>
          world.unitsOnMap = world.unitsOnMap.updated(y, world.unitsOnMap(y).updated(x, Some(unit)))
          (List(Move(x, y, unit, ticksLeft - 1, ticksOverall)), UpdateUnitData(Map(), Map(), List()))
      }
    } else {
      // check invariant
      assert(world.unitsOnMap(y)(x) == Some(unit))

      val nextMove = Move(x, y, unit, ticksLeft - 1, ticksOverall)

      // unit leave its previous cell
      if (ticksLeft == ticksOverall / 2) {
        // todo method in World class
        world.unitsOnMap = world.unitsOnMap.updated(unit.y, world.unitsOnMap(unit.y).updated(unit.x, None))

        var changeMap: Map[String, String] = Map()

        if (unit.x != x) changeMap += "x" -> String.valueOf(x)
        if (unit.y != y) changeMap += "y" -> String.valueOf(y)

        unit.x = x
        unit.y = y

        (List(nextMove), UpdateUnitData(Map(), Map(unit.id -> changeMap), List()))
      } else (List(nextMove), UpdateUnitData(Map(), Map(), List()))
    }
  }
}

case class Attack(unit: Unit, ticksLeft: Int) extends AtomicAction {
  def spentTick(world: World): List[AtomicAction] = ???
}