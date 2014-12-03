package game.unit

import format.pud.PudCodec
import format.pud.PudCodec.Position
import game.world._
import game.{Game, Human, Neutral, Orc}
import models.unit.UnitCharacteristic

class Unit(val id: Int, pudUnit: PudCodec.Unit, val player: Player, val name: String, val ch: UnitCharacteristic) {
  type ActionsType = List[AtomicAction]

  var x = pudUnit.position.x
  var y = pudUnit.position.y
  val data = pudUnit.data
  val isBuilding: Boolean = pudUnit.Type > 58

  val width = ch.pudUc.basic.unitSize._1
  val height = ch.pudUc.basic.unitSize._2

  var hp = ch.pudUc.basic.hitPoints
  var armor = ch.pudUc.basic.armor
  var sightRange: Int = ch.pudUc.basic.sightRange.toInt

  var atomicAction: ActionsType = List(Still(this))

  def executeAction(game: Game): Set[Change] = {
    atomicAction match {
      case action :: hs =>
        action.execute(game, hs)
    }
  }

  /** in original warcraft 2 sight center of unit appears at bottom right center of unit sprite */
  def centerCoords = Position((x + width) * 32 - 16, (y + height) * 32 - 16)

  // todo implement case when unit width & height > 1
  def isVisible(vision: Array[Array[Int]]): Boolean = vision(y)(x) > 0

  def getVisibility: Set[TileVisibility] = {
    val center = centerCoords

    def sqr(x: Int) = x * x

    def visibility(coverAll: Boolean, coverAny: Boolean): Visibility = {
      if (coverAll) FullVisible
      else if (coverAny) HalfVisible
      else NonVisible
    }

    val visibilitySeq = for {
      x <- center.x - sightRange to center.x + sightRange
      y <- center.y - sightRange to center.y + sightRange
      upRightDist = sqr(2 * x - 1 - 2 * center.x) + sqr(2 * y - 1 - 2 * center.y)
      upLeftDist = sqr(2 * x + 1 - 2 * center.x) + sqr(2 * y - 1 - 2 * center.y)
      downRightDist = sqr(2 * x - 1 - 2 * center.x) + sqr(2 * y + 1 - 2 * center.y)
      downLeftDist = sqr(2 * x + 1 - 2 * center.x) + sqr(2 * y + 1 - 2 * center.y)

      corners: List[Int] = List(upRightDist, upLeftDist, downRightDist, downLeftDist)
      coveredCorners = corners filter { n: Int => n <= 4 * sqr(sightRange) }
    } yield TileVisibility(x, y, visibility(coveredCorners.size == corners.size, coveredCorners.size > 0))

    (visibilitySeq filter { _.visibility != NonVisible }).toSet
  }
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