package game.unit

import game.{Game, Stop}

case class Still(unit: Unit) extends AtomicAction {
  val ticksLeft = 1
  val order = Stop

  def execute(game: Game, rest: Unit#ActionsType): Set[Change] = rest match {
    case x :: xs => Set(UnitActionsChange(unit, unit.atomicAction.tail))
    case Nil => Set()
  }

  def isChanged(aa: AtomicAction): Boolean = aa match {
    case Still(_) => false
    case _ => true
  }

  def change = Set(("action", "still"))
}
