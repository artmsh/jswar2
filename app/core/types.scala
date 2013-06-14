package core

import util.Random

trait RandomValue extends Enumeration {
  val RANDOM, DEFAULT = Value

  def withName0(s: String) : Option[Value] = s match {
    case "RANDOM" => Some(apply(Random.nextInt(values.size)))
    case "DEFAULT" => None
    case _ => Some(super.withName(s))
  }
}

object Race extends Enumeration with RandomValue {
  type Race = Value
  // NEUTRAL value added to avoid duplicating enums
  val ORC, HUMAN, NEUTRAL = Value
}

object Resources extends Enumeration with RandomValue {
  type Resources = Value
  val LOW, MEDIUM, HIGH = Value

  /**
   * @return (gold,lumber,oil)
   */
  def getResourcesAmount(r: Option[Resources], default: (Int, Int, Int)): (Int, Int, Int) = r match {
    case Some(LOW) => (2000, 1000, 1000)
    case Some(MEDIUM) => (5000, 2000, 2000)
    case Some(HIGH) => (10000, 5000, 5000)
    case None => default
  }
}

object Units extends Enumeration with RandomValue {
  type Units = Value
  val PEASANT_ONLY = Value
}

object Opponents extends Enumeration with RandomValue {
  type Opponents = Value
  val OP1, OP2, OP3, OP4, OP5, OP6, OP7 = Value
}

object Tileset extends Enumeration with RandomValue {
  type Tileset = Value
  val SUMMER, WINTER, WASTELAND, SWAMP = Value
}