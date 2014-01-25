package format.pud

import org.specs2.mutable.Specification

class PudCodecSpec extends Specification {
  "PudCodec" should {
    "have proper number of players" in {
      Pud("conf/maps/multi/TANDALOS.PUD") map { _.numPlayers } must equalTo(Some(7))
    }
  }
}
