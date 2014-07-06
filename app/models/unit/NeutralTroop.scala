package models.unit

abstract class NeutralTroop extends Troop {
}

object Critter extends NeutralTroop {
  val sightRange: Long = 2
  val hitPoints: Int = 5
  val isMagic: Boolean = false
  val buildTime: Int = _
  val goldCost: Int = 0
  val lumberCost: Int = 0
  val oilCost: Int = 0
  val boxSize: (Int, Int) = _
  val attackRange: Int = _
  val armor: Int = _
  val selectableViaRectangle: Boolean = _
  val priority: Int = _
  val basicDamage: Int = _
  val piercingDamage: Int = _
  val weaponsUpgradable: Boolean = _
  val armorUpgradable: Boolean = _
  val missileWeapon: Option[Missile] = None
  val kind: Kind = Land
  val secondMouseBtnAction: Option[MouseBtnAction] = _
  val pointsForKilling: Int = _
  val canTarget: CanTarget = _
}
