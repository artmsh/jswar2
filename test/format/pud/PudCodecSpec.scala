package format.pud

import org.specs2.mutable.Specification

class PudCodecSpec extends Specification {
  "PudCodec" should {
    "have proper number of players" in {
      Pud("maps/multi/TANDALOS.PUD") map { _.numPlayers } must equalTo(7)
    }
  }
}
