package format.pud

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

trait Kind
case object Land extends Kind
case object Fly extends Kind
case object Naval extends Kind