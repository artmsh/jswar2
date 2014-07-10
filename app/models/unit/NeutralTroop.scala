package models.unit

abstract class NeutralTroop extends Troop {
}

object Critter extends NeutralTroop {
  val sightRange: Long = 2
  val hitPoints: Int = 5
  val isMagic: Boolean = false
  val buildTime: Int = 0
  val goldCost: Int = 0
  val lumberCost: Int = 0
  val oilCost: Int = 0
  val boxSize: (Int, Int) = (31, 31)
  val attackRange: Int = 0
  val armor: Int = 0
  val selectableViaRectangle: Boolean = false
  val priority: Int = 37
  val basicDamage: Int = 0
  val piercingDamage: Int = 0
  val weaponsUpgradable: Boolean = false
  val armorUpgradable: Boolean = false
  val missileWeapon: Option[Missile] = None
  val kind: Kind = Land
  val secondMouseBtnAction: Option[MouseBtnAction] = None
  val pointsForKilling: Int = 1
  val canTarget: CanTarget = new CanTarget(1)
}
