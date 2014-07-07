package game.unit

trait Change
case class UnitPositionChange(unit: Unit, position: (Int, Int)) extends Change
case class UnitActionsChange(unit: Unit, actions: Unit#ActionsType) extends Change
case class UnitOccupyCell(unit: Unit, position: (Int, Int)) extends Change