package models

import core.{Resources, Race, Opponents, Terrain}
import models.format.{PlayerType, Pud}
import controllers.SinglePlayerSetting
import scala.util.Random
import models.format.PudCodec.Pud
import models.format.Pud

class World(val players: IndexedSeq[Player], val units: IndexedSeq[models.unit.Unit], val terrain: Terrain) {
  def diff(world: World, currentPlayer: Player): UpdateData = {

    new UpdateData()
  }

  def update(events: ClientEvents): World = {

    new World(players, units, terrain)
  }
}

object World {
  def pickRandom[T](seq: Seq[T]): T = seq(Random.nextInt(seq.length))

  def resourcesAmount(resources: Resources.Resources, pud: Pud, playerIndex: Int) = resources match {
    case Resources.RANDOM => Resources.getResourcesAmount(Resources(Random.nextInt(3) + 2))
    case Resources.DEFAULT => (pud.sgld._2.gold(playerIndex), pud.slbr._2.lumber(playerIndex), pud.soil._2.oil(playerIndex))
    case r => Resources.getResourcesAmount(r)
  }

  def startPos(pud: Pud, playerIndex: Int): (Int, Int) = {
    pud.unit._2.units
      .filter(unit => unit.Type == 0x5e || unit.Type == 0x5f)
      .find(_.player == playerIndex)
      .map(u => (u.x, u.y)).get
  }

  def apply(settings: SinglePlayerSetting): World = {
    val pud = Pud(settings.mapFileName).toOption.get

    assert(pud.players.count(_ == PlayerType.COMPUTER) > 0)
    assert(pud.players.count(_ == PlayerType.PERSON) > 0)

    val opponents: Int = Opponents.applied(settings.opponents,
      Opponents(pud.players.count(_ == PlayerType.COMPUTER) + 1)).id - 1

    val indexedPlayers = (0 until pud.players.length).zip(pud.players)
    val personPlayer = pickRandom(indexedPlayers)

    val players = for {
      player <- indexedPlayers.sorted(new Ordering[(Int, PlayerType.Value)] {
        def compare(x: (Int, PlayerType.Value), y: (Int, PlayerType.Value)): Int = x._2.compare(y._2)
      }).take(opponents + 1)
    } yield {
      if (player == personPlayer)
        new Player(player._1, PlayerType.PERSON, Race.applied(settings.race, pud.side._2.playerSlots(player._1)),
          resourcesAmount(settings.resources, pud, player._1), startPos(pud, player._1))
      else new Player(player._1, PlayerType.COMPUTER, pud.side._2.playerSlots(player._1),
        resourcesAmount(settings.resources, pud, player._1), startPos(pud, player._1))
    }

    new World(players, pud.unit._2.units.flatMap(unit.Unit.fromPudUnit), null)
  }
}

object EmptyWorld extends World(IndexedSeq(), IndexedSeq(), new Terrain(Array(), 0, 0, Seq())) {

}