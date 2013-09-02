package models

import core.{Resources, Race, Opponents}
import models.format.PlayerType
import controllers.SinglePlayerSetting
import scala.util.Random
import models.format.PudCodec.Pud
import models.terrain.Terrain

class World(val players: IndexedSeq[Player], val units: IndexedSeq[models.unit.Unit[_]], val terrain: Terrain) {
  def diff(world: World, currentPlayer: Player): UpdateData = {

    new UpdateData()
  }

  def update(events: List[ClientEvent]): World = {

    new World(players, units, terrain)
  }

  def nextTurn(): World = {
    new World(
      players,
      units.map { unit =>
        unit.orders.head.remainingTime match {
          case 0 => unit.orders = unit.orders.tail
          case -1 =>
          case _ => unit.orders.head.remainingTime--
        }
        unit
      },
      terrain
    )
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
      .filter(_.isStartLocation)
      .find(_.player == playerIndex)
      .map(u => (u.x, u.y)).get
  }

  def apply(settings: SinglePlayerSetting): World = {
    val pud = models.format.Pud(settings.mapFileName).toOption.get

    assert(pud.players.count(_ == PlayerType.COMPUTER) > 0)
    assert(pud.players.count(_ == PlayerType.PERSON) > 0)

    val opponents: Int = Opponents.applied(settings.opponents,
      Opponents(pud.players.count(_ == PlayerType.COMPUTER) + 1)).id - 1

    val indexedPlayers = (0 until pud.players.length).zip(pud.players)
    val personPlayer = pickRandom(indexedPlayers.filter(_._2 == PlayerType.PERSON))

    val players: IndexedSeq[Player] = indexedPlayers.sorted(new Ordering[(Int, PlayerType.Value)] {
        def compare(x: (Int, PlayerType.Value), y: (Int, PlayerType.Value)): Int = x._2.compare(y._2)
      }).take(opponents).map(player =>
        new Player(player._1, PlayerType.COMPUTER, pud.side._2.playerSlots(player._1),
          resourcesAmount(settings.resources, pud, player._1), startPos(pud, player._1))
      ) :+ new Player(personPlayer._1, PlayerType.PERSON, Race.applied(settings.race, pud.side._2.playerSlots(personPlayer._1)),
        resourcesAmount(settings.resources, pud, personPlayer._1), startPos(pud, personPlayer._1))

    new World(players, pud.unit._2.units.filter(!_.isStartLocation)
                                        .flatMap(unit.Unit.fromPudUnit),
      new Terrain(pud.tiles, pud.mapSizeX, pud.mapSizeY))
  }
}

object EmptyWorld extends World(IndexedSeq(), IndexedSeq(), new Terrain(IndexedSeq(), 0, 0)) {

}