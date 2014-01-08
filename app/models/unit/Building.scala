package models.unit

abstract class Building extends UnitCharacteristic {
  val armor: Int = 20
  val sightRange: Int = 1
  val isMagic = false
  val attackRange = 0
  val selectableViaRectangle: Boolean = false
  val weaponsUpgradable: Boolean = false
  val armorUpgradable: Boolean = false
  val missileWeapon: Option[Missile] = None
  val secondMouseBtnAction: Option[MouseBtnAction] = None
  val basicDamage: Int = 0
  val piercingDamage: Int = 0
  val canTarget: CanTarget = new CanTarget(0)
  val reactionRangeComputer: Int = _
  val reactionRangeHuman: Int = _
  // todo don't make any sense
  val decayRate: Int = _
  val flags: Long = _
}

class AbstractFarm extends Building {
  override val sightRange: Int = 2
  val hitPoints: Int = 400
  val buildTime: Int = 100
  val goldCost: Int = 500
  val lumberCost: Int = 250
  val oilCost: Int = 0
  val unitSize: (Int, Int) = (2, 2)
  val boxSize: (Int, Int) = (63, 63)
  val priority: Int = 20
  val kind: Kind = Land
  val annoyFactor: Int = 45
  val pointsForKilling: Int = 100
}

class Barracks extends Building {
  val hitPoints: Int = 800
  val buildTime: Int = 200
  val goldCost: Int = 700
  val lumberCost: Int = 450
  val oilCost: Int = 0
  val unitSize: (Int, Int) = (3, 3)
  val boxSize: (Int, Int) = (95, 95)
  val priority: Int = 30
  val kind: Kind = Land
  val annoyFactor: Int = 35
  val pointsForKilling: Int = 160
}

class LumberMill extends Building {
  val hitPoints: Int = 600
  val buildTime: Int = 150
  val goldCost: Int = 600
  val lumberCost: Int = 450
  val oilCost: Int = 0
  val unitSize: (Int, Int) = (3, 3)
  val boxSize: (Int, Int) = (95, 95)
  val priority: Int = 25
  val kind: Kind = Land
  val annoyFactor: Int = 15
  val pointsForKilling: Int = 150
}

class Blacksmith extends Building {
  val hitPoints: Int = 775
  val buildTime: Int = 200
  val goldCost: Int = 800
  val lumberCost: Int = 450
  val oilCost: Int = 100
  val unitSize: (Int, Int) = (3, 3)
  val boxSize: (Int, Int) = (95, 95)
  val priority: Int = 15
  val kind: Kind = Land
  val annoyFactor: Int = 20
  val pointsForKilling: Int = 170
}

class WatchTower extends Building {
  override val sightRange = 9

  val hitPoints: Int = 100
  val buildTime: Int = 60
  val goldCost: Int = 550
  val lumberCost: Int = 200
  val oilCost: Int = 0
  val unitSize: (Int, Int) = (2, 2)
  val boxSize: (Int, Int) = (63, 63)
  val priority: Int = 55
  val kind: Kind = Land
  val annoyFactor: Int = 50
  val pointsForKilling: Int = 95
}