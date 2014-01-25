package game.world

import core.Resources
import scala.util.Random
import format.pud.Pud
import game.unit.Unit

// Care: don't remove entries from units vector
class World(var playerStats: Map[Int, PlayerStats], var units: Vector[Unit], var terrain: Terrain) {
  var unitsOnMap: Vector[Vector[Option[Unit]]] = _unitsOnMap

  def _unitsOnMap = Vector.tabulate(terrain.height, terrain.width)((y, x) => {
    units.find(u => x >= u.x && x < u.x + u.width && y >= u.y && y < u.y + u.height)
  })

  def spentTick(): List[Int] = ???

  def updateDataFull(player: Int): UpdateData = {
    val playerUnits = units.filter(_.player == player)
    val vision = terrain.getVision(playerUnits)

    val addedTerrain = (for {
      i <- 0 until vision.length
      j <- 0 until vision(i).length
      if (vision(i)(j)) != 0
    } yield ((j, i, terrain.tiles(i)(j), vision(i)(j)), unitsOnMap(i)(j))).toList

    val visibleUnits = addedTerrain.flatMap(_._2).distinct
    UpdateData(
      (0 until visibleUnits.length).zip(visibleUnits).toMap, Map(), List(),
      playerStats(player),
      addedTerrain.map(_._1), List()
    )
  }
}

object World {
  def pickRandom[T](seq: Seq[T]): T = seq(Random.nextInt(seq.length))

  def resourcesAmount(resources: Resources.Resources, pud: Pud, playerIndex: Int) = resources match {
    case Resources.RANDOM => Resources.getResourcesAmount(Resources(Random.nextInt(3) + 2))
    case Resources.DEFAULT => (pud._pud.sgld._2.gold(playerIndex), pud._pud.slbr._2.lumber(playerIndex), pud._pud.soil._2.oil(playerIndex))
    case r => Resources.getResourcesAmount(r)
  }

  def startPos(pud: Pud, playerIndex: Int): (Int, Int) = {
    pud._pud.unit._2.units
      .filter(_.isStartLocation)
      .find(_.player == playerIndex)
      .map(u => (u.x, u.y)).get
  }

//  def apply(settings: SinglePlayerSetting): World = {
//    val pud = format.pud.Pud(settings.mapFileName).toOption.get
//
//    assert(pud.players.count(_ == Computer) > 0)
//    assert(pud.players.count(_ == Person) > 0)
//
//    val opponents: Int = Opponents.applied(settings.opponents,
//      Opponents(pud.players.count(_ == Computer) + 1)).id - 1
//
//    val indexedPlayers = (0 until pud.players.length).zip(pud.players)
//    val personPlayer = pickRandom(indexedPlayers.filter(_._2 == Person))
//
//    val players: IndexedSeq[PlayerStats] = indexedPlayers.sorted(new Ordering[(Int, PlayerType)] {
//        def compare(x: (Int, PlayerType), y: (Int, PlayerType)): Int = x._2.compare(y._2)
//      }).take(opponents).map(player =>
//        new PlayerStats(player._1, Computer, pud.side._2.playerSlots(player._1),
//          resourcesAmount(settings.resources, pud, player._1), startPos(pud, player._1))
//      ) :+ new PlayerStats(personPlayer._1, Person, Race.applied(settings.race, pud.side._2.playerSlots(personPlayer._1)),
//        resourcesAmount(settings.resources, pud, personPlayer._1), startPos(pud, personPlayer._1))
//
//    new World(players, pud.unit._2.units.filter(!_.isStartLocation)
//                                        .flatMap(unit.Unit.fromPudUnit),
//      new Terrain(pud.tiles, pud.mapSizeX, pud.mapSizeY))
//  }
}