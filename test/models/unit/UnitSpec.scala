package models.unit

import org.specs2.mutable.Specification

class UnitSpec extends Specification {
  "toClassName method" should {
    "transform 3 section unit type" in {
      Unit.toClassName("unit-pig-farm") must equalTo("PigFarm")
    }

    "transform 2 section unit type" in {
      Unit.toClassName("unit-farm") must equalTo("Farm")
    }
  }
}
