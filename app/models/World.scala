package models

import core.{Resources, Race, Opponents}
import controllers.SinglePlayerSetting
import scala.util.Random
import models.terrain.Terrain
import format.pud.PudCodec.Pud
import format.pud.{PlayerType, Computer, Person}

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
        unit.atomicAction = unit.atomicAction.spentTick(this, unit)
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
    val pud = format.pud.Pud(settings.mapFileName).toOption.get

    assert(pud.players.count(_ == Computer) > 0)
    assert(pud.players.count(_ == Person) > 0)

    val opponents: Int = Opponents.applied(settings.opponents,
      Opponents(pud.players.count(_ == Computer) + 1)).id - 1

    val indexedPlayers = (0 until pud.players.length).zip(pud.players)
    val personPlayer = pickRandom(indexedPlayers.filter(_._2 == Person))

    val players: IndexedSeq[Player] = indexedPlayers.sorted(new Ordering[(Int, PlayerType)] {
        def compare(x: (Int, PlayerType), y: (Int, PlayerType)): Int = x._2.compare(y._2)
      }).take(opponents).map(player =>
        new Player(player._1, Computer, pud.side._2.playerSlots(player._1),
          resourcesAmount(settings.resources, pud, player._1), startPos(pud, player._1))
      ) :+ new Player(personPlayer._1, Person, Race.applied(settings.race, pud.side._2.playerSlots(personPlayer._1)),
        resourcesAmount(settings.resources, pud, personPlayer._1), startPos(pud, personPlayer._1))

    new World(players, pud.unit._2.units.filter(!_.isStartLocation)
                                        .flatMap(unit.Unit.fromPudUnit),
      new Terrain(pud.tiles, pud.mapSizeX, pud.mapSizeY))
  }
}

object EmptyWorld extends World(IndexedSeq(), IndexedSeq(), new Terrain(IndexedSeq(), 0, 0)) {

}