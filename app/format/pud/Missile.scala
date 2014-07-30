package format.pud

object Missile {
  def apply(n: Int): Missile = n match {
    case 0x00 => lightning
    case 0x01 => griffon_hammer
    case 0x02 => dragon_breath
    case 0x03 => flame_shield
    case 0x07 => big_cannon
    case 0x0a => touch_of_death
    case 0x0d => catapult_rock
    case 0x0e => ballista_bolt
    case 0x0f => arrow
    case 0x10 => axe
    case 0x11 => submarine_missile
    case 0x12 => turtle_missile
    case 0x18 => small_cannon
    case 0x1b => demon_fire
    case 0x1d => none
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
case object none extends Missile