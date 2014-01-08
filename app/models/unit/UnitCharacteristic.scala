package models.unit

trait UnitCharacteristic {
  val sightRange: Long
  val hitPoints: Int
  val isMagic: Boolean
  val buildTime: Int
  val goldCost: Int
  val lumberCost: Int
  val oilCost: Int
  val unitSize: (Int, Int)
  val boxSize: (Int, Int)
  val attackRange: Int
  val reactionRangeComputer: Int
  val reactionRangeHuman: Int
  val armor: Int
  val selectableViaRectangle: Boolean
  val priority: Int
  val basicDamage: Int
  val piercingDamage: Int
  val weaponsUpgradable: Boolean
  val armorUpgradable: Boolean
  val missileWeapon: Option[Missile]
  val kind: Kind
  val decayRate: Int
  val annoyFactor: Int
  val secondMouseBtnAction: Option[MouseBtnAction]
  val pointsForKilling: Int
  val canTarget: CanTarget
  val flags: Long
}

class CanTarget(val b: Int) extends AnyVal {
  def land = (b & 1) == 1
  def sea = (b & 2) == 2
  def air = (b & 4) == 4
}

trait Kind
case object Land extends Kind
case object Fly extends Kind
case object Naval extends Kind

trait Missile
case object lightning extends Missile
case object griffon_hammer extends Missile
case object dragon_breath extends Missile
case object flame_shield extends Missile
case object flame_shield_self extends Missile
case object blizzard extends Missile
case object death_and_decay extends Missile
case object big_cannon extends Missile
case object black_powder extends Missile
case object heal_effect extends Missile
case object touch_of_death extends Missile
case object rune extends Missile
case object tornado extends Missile
case object catapult_rock extends Missile
case object ballista_bolt extends Missile
case object arrow extends Missile
case object axe extends Missile
case object submarine_missile extends Missile
case object turtle_missile extends Missile
case object dark_flame extends Missile
case object bright_flame extends Missile
case object blood extends Missile
case object more_black_powder extends Missile
case object explosion extends Missile
case object small_cannon extends Missile
case object metal_spark extends Missile
case object mini_explosion extends Missile
case object demon_fire extends Missile
case object green_cross extends Missile

trait MouseBtnAction
case object attack extends MouseBtnAction
case object move extends MouseBtnAction
case object harvest extends MouseBtnAction
case object haul_oil extends MouseBtnAction
case object demolish extends MouseBtnAction
case object sail extends MouseBtnAction
