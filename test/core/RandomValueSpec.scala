package core

import org.specs2.mutable.Specification

class RandomValueSpec extends Specification {
  "applied method" should {
    "properly check random" in {
      Opponents.applied(Opponents.RANDOM, Opponents.OP1) must beOneOf(Opponents.values.drop(2))
    }
    "properly check default value" in {
      Opponents.applied(Opponents.DEFAULT, Opponents.OP1) must beEqualTo(Opponents.OP1)
    }
    "properly check initial value" in {
      Opponents.applied(Opponents.OP2, Opponents.OP1) must beEqualTo(Opponents.OP2)
    }
  }
}
