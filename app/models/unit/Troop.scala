package models.unit

import models.action._
import models.action.MapTargetParam
import scala.Some
import models.format.CanTarget

abstract class Troop extends UnitCharacteristic {
  val unitSize: (Int, Int) = (1, 1)
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
  val sightRange = 4
  val isMagic: Boolean = false
  val buildTime: Int = 60
  val goldCost: Int = 600
  val lumberCost: Int = 0
  val boxSize: (Int, Int) = (31, 31)
  val attackRange: Int = 1
  val reactionRangeComputer: Int = _
  val reactionRangeHuman: Int = _
  val priority: Int = 60
  val basicDamage: Int = 6
  val piercingDamage: Int = 3
  val missileWeapon: Option[Missile] = None
  val decayRate: Int = _
  val annoyFactor: Int = _
  val secondMouseBtnAction: MouseBtnAction = attack
  val pointsForKilling: Int = 50
  val canTarget: CanTarget = new CanTarget(1)
  val flags: Long = _
}

class Bowman extends LandTroop {
  val armor: Int = 0
  val moveSpeed: Int = 10
  val sightRange: Int = 5
  val hitPoints: Int = 40
  val isMagic: Boolean = false
  val buildTime: Int = 70
  val goldCost: Int = 500
  val lumberCost: Int = 50
  val boxSize: (Int, Int) = (33, 33)
  val attackRange: Int = 4
  val reactionRangeComputer: Int = 7
  val reactionRangeHuman: Int = 5
  val priority: Int = 55
  val basicDamage: Int = 3
  val piercingDamage: Int = 6
  val missileWeapon: Option[Missile] = Some(arrow)
  val decayRate: Int = _
  val annoyFactor: Int = _
  val secondMouseBtnAction: Option[MouseBtnAction] = Some(attack)
  val pointsForKilling: Int = 60
  val canTarget: CanTarget = new CanTarget(7)
  val flags: Long = _
}

class AbstractCatapult extends LandTroop {
  val armor: Int = 0
  val moveSpeed: Int = 5
  val sightRange: Int = 9
  val hitPoints: Int = 110
  val isMagic: Boolean = false
  val buildTime: Int = 250
  val goldCost: Int = 900
  val lumberCost: Int = 300
  val boxSize: (Int, Int) = (63, 63)
  val attackRange: Int = 8
  val reactionRangeComputer: Int = 11
  val reactionRangeHuman: Int = 9
  val priority: Int = 70
  val basicDamage: Int = 80
  val piercingDamage: Int = 0
  val missileWeapon: Option[Missile] = Some(catapult_rock)
  val decayRate: Int = _
  val annoyFactor: Int = _
  val secondMouseBtnAction: Option[MouseBtnAction] = Some(attack)
  val pointsForKilling: Int = 100
  val canTarget: CanTarget = new CanTarget(3)
  val flags: Long = _
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
  val reactionRangeComputer: Int = _
  val reactionRangeHuman: Int = _
  val armor: Int = 4
  val priority: Int = 63
  val basicDamage: Int = 8
  val piercingDamage: Int = 4
  val missileWeapon: Option[Missile] = None
  val decayRate: Int = _
  val annoyFactor: Int = _
  val secondMouseBtnAction: Option[MouseBtnAction] = Some(attack)
  val pointsForKilling: Int = 100
  val canTarget: CanTarget = new CanTarget(1)
  val flags: Long = _
}