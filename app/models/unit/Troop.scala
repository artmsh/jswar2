package models.unit

import scala.Some

abstract class Troop extends UnitCharacteristic {
  val unitSize: (Int, Int) = (1, 1)
  // todo fix
  val flags: Long = 0
  val decayRate = 0
  val annoyFactor = 0
  val reactionRangeComputer: Int = -1
  val reactionRangeHuman: Int = -1
}

abstract class LandTroop(val kind: Kind = Land, val oilCost: Int = 0,
                         val selectableViaRectangle: Boolean = true,
                         val weaponsUpgradable: Boolean = true,
                         val armorUpgradable: Boolean = true) extends Troop { }
abstract class AirTroop(kind: Kind = Fly) extends Troop { }
abstract class SeaTroop(kind: Kind = Naval) extends Troop { }

class Swordsman extends LandTroop {
  val hitPoints = 60
  val armor = 2
  val sightRange: Long = 4
  val isMagic: Boolean = false
  val buildTime: Int = 60
  val goldCost: Int = 600
  val lumberCost: Int = 0
  val boxSize: (Int, Int) = (31, 31)
  val attackRange: Int = 1
  val priority: Int = 60
  val basicDamage: Int = 6
  val piercingDamage: Int = 3
  val missileWeapon: Option[Missile] = None
  val secondMouseBtnAction: Option[MouseBtnAction] = Some(attack)
  val pointsForKilling: Int = 50
  val canTarget: CanTarget = new CanTarget(1)
}

class Bowman extends LandTroop {
  val armor: Int = 0
  val moveSpeed: Int = 10
  val sightRange: Long = 5
  val hitPoints: Int = 40
  val isMagic: Boolean = false
  val buildTime: Int = 70
  val goldCost: Int = 500
  val lumberCost: Int = 50
  val boxSize: (Int, Int) = (33, 33)
  val attackRange: Int = 4
  override val reactionRangeComputer: Int = 7
  override val reactionRangeHuman: Int = 5
  val priority: Int = 55
  val basicDamage: Int = 3
  val piercingDamage: Int = 6
  val missileWeapon: Option[Missile] = Some(arrow)
  val secondMouseBtnAction: Option[MouseBtnAction] = Some(attack)
  val pointsForKilling: Int = 60
  val canTarget: CanTarget = new CanTarget(7)
}

class AbstractCatapult extends LandTroop {
  val armor: Int = 0
  val moveSpeed: Int = 5
  val sightRange: Long = 9
  val hitPoints: Int = 110
  val isMagic: Boolean = false
  val buildTime: Int = 250
  val goldCost: Int = 900
  val lumberCost: Int = 300
  val boxSize: (Int, Int) = (63, 63)
  val attackRange: Int = 8
  override val reactionRangeComputer: Int = 11
  override val reactionRangeHuman: Int = 9
  val priority: Int = 70
  val basicDamage: Int = 80
  val piercingDamage: Int = 0
  val missileWeapon: Option[Missile] = Some(catapult_rock)
  val secondMouseBtnAction: Option[MouseBtnAction] = Some(attack)
  val pointsForKilling: Int = 100
  val canTarget: CanTarget = new CanTarget(3)
}

class Knight extends LandTroop {
  val sightRange: Long = 4
  val hitPoints: Int = 90
  val isMagic: Boolean = false
  val buildTime: Int = 90
  val goldCost: Int = 800
  val lumberCost: Int = 100
  val boxSize: (Int, Int) = (42, 42)
  val attackRange: Int = 1
  val armor: Int = 4
  val priority: Int = 63
  val basicDamage: Int = 8
  val piercingDamage: Int = 4
  val missileWeapon: Option[Missile] = None
  val secondMouseBtnAction: Option[MouseBtnAction] = Some(attack)
  val pointsForKilling: Int = 100
  val canTarget: CanTarget = new CanTarget(1)
}