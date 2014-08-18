package game.unit

import game.Order
import game.world.World

/* precompute A* if not precomputed and if we reach non-empty field - we compute it again */
case class Move(x: Int, y: Int, unit: Unit, order: Order, ticksLeft: Int, ticksOverall: Int) extends AtomicAction {
  require(ticksLeft > 0)

  def spentTick(world: World, rest: Unit#ActionsType): Set[_ >: Change] = {
    assert(ticksLeft > 0)

    if (ticksLeft == ticksOverall) {
      world.unitsOnMap(y)(x) match {
        case Some(_) => Set(UnitActionsChange(unit, Still(unit) :: rest))
        case None =>
          Set(UnitOccupyCell(unit, (x, y)), UnitActionsChange(unit, Move(x, y, unit, order, ticksLeft - 1, ticksOverall) :: rest))
      }
    } else {
      // check invariant
      assert(world.unitsOnMap(y)(x) == Some(unit))

      var nextMoves = rest
      if (ticksLeft > 1) {
        nextMoves = Move(x, y, unit, order, ticksLeft - 1, ticksOverall) :: nextMoves
        Set(UnitActionsChange(unit, nextMoves))
      } else {
        // unit leave its previous cell
        Set(UnitPositionChange(unit, (x, y)), UnitActionsChange(unit, nextMoves))
      }
    }
  }

  // change don't include order changes
  def isChanged(aa: AtomicAction): Boolean = aa match {
    case Move(_x, _y, _, _, _, _) => _x != x || _y != y
    case _ => true
  }

  def change: Set[(String, String)] = Set(("action", "move"), ("moveX", String.valueOf(x)), ("moveY", String.valueOf(y)))
}
