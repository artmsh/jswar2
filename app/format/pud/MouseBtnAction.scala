package format.pud

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

trait MouseBtnAction
case object attack extends MouseBtnAction
case object move extends MouseBtnAction
case object harvest extends MouseBtnAction
case object haul_oil extends MouseBtnAction
case object demolish extends MouseBtnAction
case object sail extends MouseBtnAction