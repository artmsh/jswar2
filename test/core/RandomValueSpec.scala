package core

import org.specs2.mutable.Specification
import controllers.Opponents

class RandomValueSpec extends Specification {
  "applied method" should {
    "properly check random" in {
      val applied: Opponents.Value = Opponents.applied(Opponents.RANDOM, Opponents.OP1)

      Opponents.values.drop(2).contains(applied) must beTrue
    }
    "properly check default value" in {
      Opponents.applied(Opponents.DEFAULT, Opponents.OP1) must beEqualTo(Opponents.OP1)
    }
    "properly check initial value" in {
      Opponents.applied(Opponents.OP2, Opponents.OP1) must beEqualTo(Opponents.OP2)
    }
  }
}
