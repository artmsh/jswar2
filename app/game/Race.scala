package game

sealed trait Race
case object Human extends Race
case object Orc extends Race
case object Neutral extends Race
object Race {
  def apply(race: String): Race = race match {
    case "Human" => Human
    case "Orc" => Orc
    case "Neutral" => Neutral
  }
}
