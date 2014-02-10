package game.world

import scala.util.Random
import format.pud.{PudCodec, Pud}
import game.unit.Unit
import controllers.Resources
import models.unit.{Land, Naval, Fly, Kind}
import game.{Neutral, unit, GameSettings}

// Care: don't remove entries from units vector
class World(var playerStats: Map[Int, PlayerStats], var units: Vector[Unit], var terrain: Terrain) {
  var unitsOnMap: Vector[Vector[Option[Unit]]] = _unitsOnMap

  // todo optimize
  def _unitsOnMap = Vector.tabulate(terrain.height, terrain.width)((y, x) => {
    units.find(u => x >= u.x && x < u.x + u.width && y >= u.y && y < u.y + u.height)
  })

  def init(pud: Pud, settings: GameSettings) {
    playerStats = Map[Int, PlayerStats]() ++ (settings.playerSettings.keySet map { num: Int =>
      (num -> new PlayerStats(num, pud.players(num),
        settings.playerSettings(num).race, pud.startingRes(num), pud.startingPos(num)))
    })

    units = Vector[Unit]() ++ pud._pud.unit._2.units
      .filter(!_.isStartLocation)
      .filter(u => settings.playerSettings.contains(u.player))
      .map(u => u match {
      case PudCodec.Unit(_,_,_,15,_) => unit.Unit(u, Neutral, pud.unitTypes)
      case u: PudCodec.Unit => unit.Unit(u, settings.playerSettings(u.player).race, pud.unitTypes)
    })

    this.terrain = new Terrain(Vector.tabulate(pud.mapSizeY, pud.mapSizeX)
      ((row, column) => pud.tiles(row * pud.mapSizeX + column)), pud.mapSizeX, pud.mapSizeY)

    this.unitsOnMap = this._unitsOnMap
  }

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

  def getUnitsPassability(player: Int, kind: Kind): ((Int, Int)) => Boolean = kind match {
    case Fly => (p => true)
    case Land => {
      val vision = terrain.getVision(units.filter(_.player == player))

      p: (Int, Int) => vision(p._1)(p._2) != 0 && unitsOnMap(p._1)(p._2).fold(true)(!_.isBuilding)
    }
    // todo implement
    case Naval => ???
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