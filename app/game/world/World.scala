package game.world

import scala.util.Random
import format.pud.{PudCodec, Pud}
import game.unit.Unit
import controllers.Resources
import models.unit.{Land, Naval, Fly, Kind}
import game.{Neutral, unit, GameSettings}
import utils.ArrayUtils

// Care: don't remove entries from units vector
class World(var playerStats: Map[Int, PlayerStats], var units: Vector[Unit], var terrain: Terrain) {
  var unitsOnMap: Vector[Vector[Option[Unit]]] = _unitsOnMap

  // now vision means map exploration
  var playerVision: Map[Int, Array[Array[Int]]] = Map()

  // todo optimize
  def _unitsOnMap = Vector.tabulate(terrain.height, terrain.width)((y, x) => {
    units.find(u => x >= u.x && x < u.x + u.width && y >= u.y && y < u.y + u.height)
  })

  def init(pud: Pud, settings: GameSettings) {
    // todo fix initializing class variables in a method
    playerStats = Map[Int, PlayerStats]() ++ (settings.playerSettings.keySet map { num: Int =>
      (num -> new PlayerStats(num, pud.players(num),
        settings.playerSettings(num).race, pud.startingRes(num), pud.startingPos(num)))
    })

   playerVision = playerStats map { p => (p._1, Array[Array[Int]]()) }

    units = Vector[Unit]() ++ pud._pud.unit._2.units
      .filter(!_.isStartLocation)
      .filter(u => settings.playerSettings.contains(u.player))
      .map(u => u match {
      case PudCodec.Unit(_,_,_,15,_) => unit.Unit(u, Neutral, pud.unitTypes)
      case u: PudCodec.Unit => unit.Unit(u, settings.playerSettings(u.player).race, pud.unitTypes)
    })

    this.terrain = new Terrain(Vector.tabulate(pud.mapSizeY, pud.mapSizeX)
      ((row, column) => pud.tiles(row * pud.mapSizeX + column)), pud.mapSizeX, pud.mapSizeY)

    playerVision = playerVision map { p => (p._1, terrain.getVision(units.filter(_.player == p._1))) }

    this.unitsOnMap = this._unitsOnMap
  }

  def unitsDiff(unit1: Unit, unit2: Unit): Map[String, String] = {
    var diff = Map[String, String]()
    if (unit1.x != unit2.x) diff += "x" -> String.valueOf(unit1.x)
    if (unit1.y != unit2.y) diff += "y" -> String.valueOf(unit1.y)
    if (unit1.hp != unit2.hp) diff += "hp" -> String.valueOf(unit1.hp)
    if (unit1.armor != unit2.armor) diff += "armor" -> String.valueOf(unit1.armor)
    val topAction1 = unit1.peekAtomicAction
    if (topAction1 != unit2.peekAtomicAction && topAction1.isDefined)
      diff += "action" -> String.valueOf(topAction1.get.getClass.getSimpleName.toLowerCase)

    // todo order change between spentTick calls
    if (unit1.order != unit2.order && unit1.order.isDefined)
      diff += "order" -> unit1.order.get.getClass.getSimpleName.toLowerCase

    diff
  }

  def playerStatsDiff(ps1: PlayerStats, ps2: PlayerStats): Map[String, String] = {
    (ps1.asMap.toList diff ps2.asMap.toList).toMap
  }

  def spentTick(): Map[Int, UpdateData] = {
    val playerStatsBefore = playerStats
    units.sortBy(_.ch.priority).foreach(_.spentTick(this))

    val newPlayerVision = playerVision map { p =>
      (p._1, terrain.mergeVision(p._2, terrain.getVision(units.filter(_.player == p._1)))) }

    val terrainDiff: Map[Int, (List[AddedTileInfo], List[UpdatedTileInfo])] = newPlayerVision map { p => {
      val visionDiff = ArrayUtils.array2DasCoords(p._2).diff(ArrayUtils.array2DasCoords(playerVision(p._1)))
      val visionCommon = ArrayUtils.array2DasCoords(playerVision(p._1)).filter(p => visionDiff.find(p1 => p._1 == p1._1 && p._2 == p1._2).isEmpty)

      (p._1,
        (
          visionDiff map { p => AddedTileInfo(p._2, p._1, terrain.tiles(p._1)(p._2), p._3) },
          visionCommon map { p => UpdatedTileInfo(p._2, p._1, terrain.tiles(p._1)(p._2)) }
        )
      )
    }}

    val unitsDiff: Map[Int, (Map[Int, Unit], Map[Int, Map[String, String]], List[Int])] = newPlayerVision map { p => {
        val newVisibleUnits = (ArrayUtils.array2DasCoords(p._2) flatMap { p => unitsOnMap(p._1)(p._2) } map { u => (u.id, u) }).toMap[Int, Unit]
        val oldVisibleUnits = (ArrayUtils.array2DasCoords(playerVision(p._1)) flatMap { p => unitsOnMap(p._1)(p._2) } map { u => (u.id, u) }).toMap[Int, Unit]

        val updatedUnits = for {
          u <- newVisibleUnits.toList.intersect(oldVisibleUnits.toList)
          u1 = newVisibleUnits(u._1)
          u2 = oldVisibleUnits(u._1)
          diffU = this.unitsDiff(u1, u2)
          if (!diffU.isEmpty)
        } yield (u._1, diffU)

        (
          p._1,
          (newVisibleUnits.toList.diff(oldVisibleUnits.toList).toMap, updatedUnits.toMap, oldVisibleUnits.toList.diff(newVisibleUnits.toList).map(_._1))
        )
      }
    }

    playerStats map { p => {
      val terrainD = terrainDiff(p._1)
      val unitsD = unitsDiff(p._1)

      (p._1, UpdateData(unitsD._1, unitsD._2, unitsD._3,
        playerStatsDiff(playerStats(p._1), playerStatsBefore(p._1)), terrainD._1, terrainD._2))
    }}
  }

  def updateDataFull(player: Int): UpdateData = {
    val vision = playerVision(player)

    val addedTerrain = (for {
      i <- 0 until vision.length
      j <- 0 until vision(i).length
      if (vision(i)(j)) != 0
    } yield (AddedTileInfo(j, i, terrain.tiles(i)(j), vision(i)(j)), unitsOnMap(i)(j))).toList

    val visibleUnits = addedTerrain.flatMap(_._2).distinct
    UpdateData(
      visibleUnits map { u => (u.id, u) } toMap, Map(), List(),
      playerStats(player).asMap,
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