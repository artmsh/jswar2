package models.unit

class Worker extends LandTroop {
  val basicDamage: Int = 3
  val piercingDamage: Int = 2
  val attackRange: Int = 1
  val armor = 0
  val sightRange: Long = 4
  val hitPoints: Int = 30
  val isMagic: Boolean = false
  val buildTime: Int = 45
  val goldCost: Int = 400
  val lumberCost: Int = 0
  val boxSize: (Int, Int) = (31, 31)
  val priority: Int = 50
  val missileWeapon: Option[Missile] = None
  val secondMouseBtnAction: Some[MouseBtnAction] = Some(harvest)
  val pointsForKilling: Int = 30
  val canTarget: CanTarget = new CanTarget(1)
}