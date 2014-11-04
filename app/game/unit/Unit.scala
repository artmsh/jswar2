package game.unit

import format.pud.PudCodec
import game.world.{Player, World}
import game.{Human, Neutral, Orc}
import models.unit.UnitCharacteristic

class Unit(val id: Int, pudUnit: PudCodec.Unit, val player: Player, val name: String, val ch: UnitCharacteristic) {
  type ActionsType = List[AtomicAction]

  var x = pudUnit.x
  var y = pudUnit.y
  val data = pudUnit.data
  val isBuilding: Boolean = pudUnit.Type > 58

  val width = ch.pudUc.basic.unitSize._1
  val height = ch.pudUc.basic.unitSize._2

  var hp = ch.pudUc.basic.hitPoints
  var armor = ch.pudUc.basic.armor
  var sightRange = ch.pudUc.basic.sightRange

  var atomicAction: ActionsType = List(Still(this))

  def spentTick(world: World): Set[_ >: Change] = {
    atomicAction match {
      case action :: hs =>
        action.spentTick(world, hs)
    }
  }

  /** in original warcraft 2 sight center of unit appears at bottom right center of unit sprite */
  def centerCoords = ((x + width) * 32 - 16, (y + height) * 32 - 16)

  // todo implement case when unit width & height > 1
  def isVisible(vision: Array[Array[Int]]): Boolean = vision(y)(x) > 0
}

object Unit {
  var counter: Int = 0

  def apply(pudUnit: PudCodec.Unit, player: Player): Unit = {
    this.synchronized {
      val u = player.race match {
        // human units are odd, orc - even
        // care: pudUnit.Type not changed
        case Human => if (pudUnit.Type % 2 == 0) new Unit(counter, pudUnit, player, player.unitTypes(pudUnit.Type)._1, player.unitTypes(pudUnit.Type)._2)
        else new Unit(counter, pudUnit, player, player.unitTypes(pudUnit.Type - 1)._1, player.unitTypes(pudUnit.Type - 1)._2)
        case Orc => if (pudUnit.Type % 2 == 1) new Unit(counter, pudUnit, player, player.unitTypes(pudUnit.Type)._1, player.unitTypes(pudUnit.Type)._2)
        else new Unit(counter, pudUnit, player, player.unitTypes(pudUnit.Type + 1)._1, player.unitTypes(pudUnit.Type + 1)._2)
        case Neutral => new Unit(counter, pudUnit, player, player.unitTypes(pudUnit.Type)._1, player.unitTypes(pudUnit.Type)._2)
      }
      counter += 1
      u
    }
  }
}