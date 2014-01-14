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
object Kind {
  def apply(n: Int): Kind = n match {
    case 0 => Land
    case 1 => Fly
    case 2 => Naval
    // some buggy cases
    case 3 => Land
    case 100 => Land
  }
}

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
object Missile {
  def apply(n: Int): Option[Missile] = n match {
    case 0x00 => Some(lightning)
    case 0x01 => Some(griffon_hammer)
    case 0x02 => Some(dragon_breath)
    case 0x03 => Some(flame_shield)
    case 0x07 => Some(big_cannon)
    case 0x0a => Some(touch_of_death)
    case 0x0d => Some(catapult_rock)
    case 0x0e => Some(ballista_bolt)
    case 0x0f => Some(arrow)
    case 0x10 => Some(axe)
    case 0x11 => Some(submarine_missile)
    case 0x12 => Some(turtle_missile)
    case 0x18 => Some(small_cannon)
    case 0x1b => Some(demon_fire)
    case 0x1d => None
  }
}

trait MouseBtnAction
case object attack extends MouseBtnAction
case object move extends MouseBtnAction
case object harvest extends MouseBtnAction
case object haul_oil extends MouseBtnAction
case object demolish extends MouseBtnAction
case object sail extends MouseBtnAction
object MouseBtnAction {
  def apply(n: Int): Option[MouseBtnAction] = n match {
    case 0 => None
    case 0x01 => Some(attack)
    case 0x02 => Some(move)
    case 0x03 => Some(harvest)
    case 0x04 => Some(haul_oil)
    case 0x05 => Some(demolish)
    case 0x06 => Some(sail)
  }
}
