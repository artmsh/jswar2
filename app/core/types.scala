package core

import util.Random

trait RandomValue extends Enumeration {
  val RANDOM, DEFAULT = Value

  def applied(startValue: Value, defaultValue: Value): Value = startValue match {
    case RANDOM => apply(Random.nextInt(values.size - 2) + 2)
    case DEFAULT => defaultValue
    case _ => startValue
  }
}

object Race extends Enumeration with RandomValue {
  type Race = Value
  // NEUTRAL value added to avoid duplicating enums
  val HUMAN, ORC, NEUTRAL = Value
}

object Resources extends Enumeration with RandomValue {
  type Resources = Value
  val LOW, MEDIUM, HIGH = Value

  /**
   * @return (gold,lumber,oil)
   */
  def getResourcesAmount(r: Resources): (Int, Int, Int) = r match {
    case LOW => (2000, 1000, 1000)
    case MEDIUM => (5000, 2000, 2000)
    case HIGH => (10000, 5000, 5000)
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