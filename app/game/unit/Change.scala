package game.unit

trait Change
case class UnitPositionChange(unit: Unit, position: (Int, Int))
case class UnitActionsChange(unit: Unit, actions: Unit#ActionsType)
case class UnitOccupyCell(unit: Unit, position: (Int, Int))