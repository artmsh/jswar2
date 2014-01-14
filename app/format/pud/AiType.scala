package format.pud

trait AiType
case object LandAttack extends AiType
case object Passive extends AiType
case object SeaAttack extends AiType
case object AirAttack extends AiType
object AiType {
  def apply(n: Int): AiType = n match {
    case 0 => LandAttack
    case 1 => Passive
    case 0x19 => SeaAttack
    case 0x1A => AirAttack
  }
}