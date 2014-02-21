package game.world

import org.specs2.mutable.Specification
import format.pud.{PudCodec, Tile}
import game.unit.Unit
import game.{unit, Orc}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

class TerrainSpec extends Specification {
  "Terrain" should {
    "compute vision correctly" in {
      val terrain = new Terrain(Vector(Vector[Tile]()), 128, 128)
      val units = List(Unit(PudCodec.Unit(10, 10, 3, 1, 0), Orc, unit.defaults))

      Future { terrain.getVision(units) } must beNull.not.await(timeout = FiniteDuration(50, TimeUnit.MILLISECONDS))
    }
  }

}
