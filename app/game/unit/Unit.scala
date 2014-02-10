package game.unit

import format.pud.PudCodec
import models.unit.UnitCharacteristic
import game.{Orc, Human, Neutral, Race, Order}
import game.world.World
import play.Logger

class Unit(pudUnit: PudCodec.Unit, val name: String, val ch: UnitCharacteristic) {
  var x = pudUnit.x
  var y = pudUnit.y
  val player = pudUnit.player
  val data = pudUnit.data
  val isBuilding: Boolean = pudUnit.Type > 58

  val width = ch.unitSize._1
  val height = ch.unitSize._2

  var hp = ch.hitPoints
  var armor = ch.armor

  var atomicAction: Iterator[AtomicAction] = Iterator.continually(Still(this))
  var order: Option[Order] = None

  def spentTick(world: World): Map[String, String] = {
    if (atomicAction.hasNext) {
      val nextAction = atomicAction.next()
      val (nextActions, updateUnitData) = nextAction.spentTick(world)


    } else {
      Logger.warn(s"AtomicAction iterator is empty for user $x, $y")
      Map()
    }

    val lastAction = atomicAction.next()
    if (lastAction.ticksLeft == 0) {
      atomicAction = atomicAction drop 1
    }

     atomicAction

    val currentAction = atomicAction.toList.head
    val (nextActions, updateUnitData) = currentAction.spentTick(world)

    if (currentAction.getClass != lastAction.getClass) {
      updateUnitData + ("action" -> currentAction.getClass.getSimpleName.toLowerCase)
    } else {
      updateUnitData
    }

    atomicAction
  }

//  def actions: MultiMap[(Action, Option[Class[_ <: Unit[T]]]), Option[ActionParam]] =
//    new MultiMap(Map[(Action, Option[Class[_ <: Unit[T]]]), Set[Option[ActionParam]]]())

  /** in original warcraft 2 sight center of unit appears at bottom right center of unit sprite */
  def centerCoords = ((x + width) * 32 - 16, (y + height) * 32 - 16)
}

object Unit {
  def apply(pudUnit: PudCodec.Unit, race: Race, unitTypes: Vector[(String, UnitCharacteristic)]): Unit = race match {
    // human units are odd, orc - even
    // care: pudUnit.Type not changed
    case Human => if (pudUnit.Type % 2 == 0) new Unit(pudUnit, unitTypes(pudUnit.Type)._1, unitTypes(pudUnit.Type)._2)
                  else new Unit(pudUnit, unitTypes(pudUnit.Type - 1)._1, unitTypes(pudUnit.Type - 1)._2)
    case Orc => if (pudUnit.Type % 2 == 1) new Unit(pudUnit, unitTypes(pudUnit.Type)._1, unitTypes(pudUnit.Type)._2)
                else new Unit(pudUnit, unitTypes(pudUnit.Type + 1)._1, unitTypes(pudUnit.Type + 1)._2)
    case Neutral => new Unit(pudUnit, unitTypes(pudUnit.Type)._1, unitTypes(pudUnit.Type)._2)
  }
}