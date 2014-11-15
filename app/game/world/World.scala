package game.world

import controllers.Resources
import format.pud._
import game.unit.{UnitActionsChange, UnitOccupyCell, UnitPositionChange, _}
import game.{GameSettings, Neutral}
import play.Logger

import scala.util.Random

// Care: don't remove entries from units vector
// units vector indexed by id
//class World(var playerStats: Map[Int, Player], var units: Vector[Unit], var terrain: Terrain) {
//  var unitsOnMap: Vector[Vector[Option[Unit]]] = _unitsOnMap
//
//  // now vision means map exploration
//  var playerExploredTerrain: Map[Int, Array[Array[Int]]] = Map()
//
//  // todo optimize
//  def _unitsOnMap = Vector.tabulate(terrain.height, terrain.width)((y, x) => {
//    units.find(u => x >= u.x && x < u.x + u.width && y >= u.y && y < u.y + u.height)
//  })
//
//  def init(pud: Pud, settings: GameSettings): Map[Int, UpdateData] = {
//    val t = System.currentTimeMillis()
//
//    // todo fix initializing class variables in a method
//    playerStats = Map[Int, Player]() ++ (settings.playerSettings.keySet map { num: Int =>
//      (num -> new Player(num, pud.players(num),
//        settings.playerSettings(num).race, pud.startingRes(num), pud.startingPos(num)))
//    })
//
//    playerExploredTerrain = playerStats map { p => (p._1, Array[Array[Int]]()) }
//
//    units = Vector[Unit]() ++ pud._pud.unit._2.units
//      .filter(!_.isStartLocation)
//      .filter(u => settings.playerSettings.contains(u.player))
//      .map(u => u match {
//      case PudCodec.Unit(_,_,_,15,_) => Unit(u, Neutral, pud.unitTypes)
//      case u: PudCodec.Unit => Unit(u, settings.playerSettings(u.player).race, pud.unitTypes)
//    })
//
//    this.terrain = new Terrain(Vector.tabulate(pud.mapSizeY, pud.mapSizeX)
//      ((row, column) => pud.tiles(row * pud.mapSizeX + column)), pud.mapSizeX, pud.mapSizeY)
//
//    val unitsByPlayer = units.groupBy(_.player)
//
//    this.unitsOnMap = this._unitsOnMap
//    val addedTerrain = playerExploredTerrain map { case (player, v) =>
//      val vision = terrain.getVision(unitsByPlayer(player))
//
//      playerExploredTerrain = playerExploredTerrain.updated(player, vision._1)
//
//      // unitsOnMap should be computed
//      val visibleUnits = for { tile <- vision._2; unit <- unitsOnMap(tile.y)(tile.x) } yield unit
//
//      (player, (vision._2, visibleUnits))
//    }
//
//    Logger.debug("init takes " + (System.currentTimeMillis() - t) + " ms")
//
//    addedTerrain map { case (player, (addedTiles, visibleUnits)) =>
//      (player,
//        UpdateData(
//          visibleUnits map { u => (u.id, u) } toMap, Map(), List(),
//          playerStats(player).asMap,
//          addedTiles, List(), List()
//        )
//      )
//    }
//  }
//
//  def measure[T](body: => T, message: String): T = {
//    val t = System.currentTimeMillis()
//    val result = body
////    Logger.debug(message + " takes " + (System.currentTimeMillis() - t) + " ms")
//    result
//  }
//
//  def filterChange[T](changeSet: Set[_ >: Change])(implicit manifest: Manifest[T]): Set[T] =
//    for { change <- changeSet; if change.getClass == manifest.runtimeClass } yield change.asInstanceOf[T]
//
//  def spentTick(): Map[Int, UpdateData] = {
//    val t = System.currentTimeMillis()
//
//    val playerStatsBefore = playerStats
//    // todo HList?
////    val changeSet = measure[Set[_ >: Change]]({
////      units.map({ unit: Unit => unit.spentTick(this) }).toSet.flatten
////    }, "units.spentTick")
//
//    val changeSet: Set[_ >: Change] = units.flatMap({ unit: Unit => unit.spentTick(this) }).toSet
//
//    val positionsToOccupy = filterChange[UnitOccupyCell](changeSet).groupBy(_.position)
//    positionsToOccupy foreach { case (p, set) =>
//      // todo max priority unit?
//      val first: UnitOccupyCell = set.head
//      unitsOnMap = unitsOnMap.updated(p._2, unitsOnMap(p._2).updated(p._1, Some(first.unit)))
//    }
//
//    val positionChanges = filterChange[UnitPositionChange](changeSet)
////    val changesPositions = changeSet.filter(_ == UnitPositionChange).groupBy(_.asInstanceOf[UnitPositionChange].position)
//    changeSet.filter(_ == UnitPositionChange) foreach { change =>
//      val upc = change.asInstanceOf[UnitPositionChange]
//      unitsOnMap = unitsOnMap.updated(upc.unit.y, unitsOnMap(upc.unit.y).updated(upc.unit.x, None))
//    }
//
//    // should be before playerExploredTerrain change
//    val unitsCameTo = playerExploredTerrain map { case (player, vision) =>
//      (player,
//        positionChanges withFilter { upc =>
//          upc.unit.player != player && !upc.unit.isVisible(vision) && vision(upc.position._2)(upc.position._1) > 0
//        } map { _.unit }
//      )
//    }
//
//    val playerUnitsChangesPosition = positionChanges.groupBy(_.unit.player)
//    val terrainDiff = playerUnitsChangesPosition map { case (player, set) =>
//      val units = set.map(_.unit).toVector
//      val vision = terrain.getVision(playerExploredTerrain(player), units)
////      playerExploredTerrain = playerExploredTerrain.updated(player, vision._1)
//
//      (player, (vision._2, vision._3))
//    }
//
//    val changedTerrain: Map[Int, List[UpdatedTileInfo]] = playerStats map { case (player, ps) => (player, List[UpdatedTileInfo]()) }
//
//    // unitsOnMap should be computed && playerExploredTerrain should be not
//    val exploredUnits: Map[Int, List[Unit]] = terrainDiff map { case (player, (addedTiles, updatedVision)) =>
//      val vision = playerExploredTerrain(player)
//      (player,
//        addedTiles withFilter { tileInfo =>
//          unitsOnMap(tileInfo.y)(tileInfo.x).fold[Boolean](false)(!_.isVisible(vision))
//        } flatMap { tileInfo =>
//          unitsOnMap(tileInfo.y)(tileInfo.x)
//        }
//      )
//    }
//
//    // todo DRY in terrainDiff
//    playerUnitsChangesPosition map { case (player, set) =>
//      val units = set.map(_.unit).toVector
//      val vision = terrain.getVision(playerExploredTerrain(player), units)
//
//      playerExploredTerrain = playerExploredTerrain.updated(player, vision._1)
//    }
//
//    // playerExploredTerrain should be computed
//    val unitsGoneFrom = playerExploredTerrain map { case (player, vision) =>
//      (player,
//        positionChanges withFilter { upc =>
//          upc.unit.player != player && upc.unit.isVisible(vision) && vision(upc.position._2)(upc.position._1) == 0
//        } map { _.unit }
//      )
//    }
//
//    val actionChanges = filterChange[UnitActionsChange](changeSet)
//
//    // units should be not computed
//    val unitsDiff = playerExploredTerrain map { case (player, vision) =>
//
//      val acceptedActions = (actionChanges.filter(_.unit.isVisible(vision))
//        .filterNot(change => exploredUnits.getOrElse(player, List[Unit]()).contains(change.unit))
//        map { change: UnitActionsChange => (change.unit, change) })
//
//      if (!acceptedActions.isEmpty) {
//        Logger.debug(acceptedActions.toString)
//      }
//
//      val affectedChanges: Set[(Unit, _ >: Change)] =
//          acceptedActions ++
//          (positionChanges.filter(_.unit.isVisible(vision))
//                          .filterNot(change => exploredUnits.getOrElse(player, List[Unit]()).contains(change.unit))
//                          map { change => (change.unit, change) })
//
//      val affectedUnits = affectedChanges.groupBy(_._1)
//      (player,
//        affectedUnits map { case (unit, unitChangeSet) =>
//          val mappedChangeSet: Set[(String, String)] = unitChangeSet flatMap { case (u, change) => change match {
//            case UnitPositionChange(_, (posX, posY)) => Set(("x", String.valueOf(posX)), ("y", String.valueOf(posY)))
//            case UnitActionsChange(_, actions) =>
//              var changes = Set[(String, String)]()
//              if (actions.head.isChanged(unit.atomicAction.head)) {
//                changes = changes ++ actions.head.change
//              }
//
//              if (actions.head.order.getClass != unit.atomicAction.head.order.getClass) {
//                changes = changes + Tuple2("order", actions.head.order.getClass.getSimpleName.toLowerCase)
//              }
//
//              changes
//          }}
//
//          (unit.id, mappedChangeSet.toMap)
//        } filter { case (unit, changeSet) => !changeSet.isEmpty }
//      )
//    }
//
//    positionChanges foreach { change =>
//      change.unit.x = change.position._1
//      change.unit.y = change.position._2
//    }
//
//    actionChanges foreach { change =>
//      change.unit.atomicAction = change.actions
//    }
//
//    val ud = playerStats map { case (player, ps) =>
//      val terrainD = terrainDiff.getOrElse(player, (List(), List()))
//      val unitsD = unitsDiff.getOrElse(player, Map[Int, Map[String, String]]())
//
//      (player, UpdateData(
//          unitsCameTo.getOrElse(player, Set[Unit]()) ++ exploredUnits.getOrElse(player, List[Unit]()) map { u => (u.id, u) } toMap,
//          unitsD,
//          unitsGoneFrom.getOrElse(player, Set[Unit]()).toList map { _.id },
//          ps diff playerStatsBefore(player),
//          terrainD._1,
//          changedTerrain(player),
//          terrainD._2
//      ))
//    }
//
////    Logger.debug("spentTick takes " + (System.currentTimeMillis() - t) + " ms")
//
//    ud
//  }
//
//  def getUnitsPassability(player: Int, kind: Kind): ((Int, Int)) => Boolean = kind match {
//    case Fly => (p => true)
//    case Land => {
//      // todo care on passability on non explored tiles
//      val vision = playerExploredTerrain(player)
//
//      p: (Int, Int) => vision(p._1)(p._2) != 0 && unitsOnMap(p._1)(p._2).fold(true)(!_.isBuilding)
//    }
//    // todo implement
//    case Naval => ???
//  }
//}
//
//object World {
//  def pickRandom[T](seq: Seq[T]): T = seq(Random.nextInt(seq.length))
//
//  def resourcesAmount(resources: Resources.Resources, pud: Pud, playerIndex: Int) = resources match {
//    case Resources.RANDOM => Resources.getResourcesAmount(Resources(Random.nextInt(3) + 2))
//    case Resources.DEFAULT => (pud._pud.sgld._2.gold(playerIndex), pud._pud.slbr._2.lumber(playerIndex), pud._pud.soil._2.oil(playerIndex))
//    case r => Resources.getResourcesAmount(r)
//  }
//
//  def startPos(pud: Pud, playerIndex: Int): (Int, Int) = {
//    pud._pud.unit._2.units
//      .filter(_.isStartLocation)
//      .find(_.player == playerIndex)
//      .map(u => (u.x, u.y)).get
//  }
//
////  def apply(settings: SinglePlayerSetting): World = {
////    val pud = format.pud.Pud(settings.mapFileName).toOption.get
////
////    assert(pud.players.count(_ == Computer) > 0)
////    assert(pud.players.count(_ == Person) > 0)
////
////    val opponents: Int = Opponents.applied(settings.opponents,
////      Opponents(pud.players.count(_ == Computer) + 1)).id - 1
////
////    val indexedPlayers = (0 until pud.players.length).zip(pud.players)
////    val personPlayer = pickRandom(indexedPlayers.filter(_._2 == Person))
////
////    val players: IndexedSeq[PlayerStats] = indexedPlayers.sorted(new Ordering[(Int, PlayerType)] {
////        def compare(x: (Int, PlayerType), y: (Int, PlayerType)): Int = x._2.compare(y._2)
////      }).take(opponents).map(player =>
////        new PlayerStats(player._1, Computer, pud.side._2.playerSlots(player._1),
////          resourcesAmount(settings.resources, pud, player._1), startPos(pud, player._1))
////      ) :+ new PlayerStats(personPlayer._1, Person, Race.applied(settings.race, pud.side._2.playerSlots(personPlayer._1)),
////        resourcesAmount(settings.resources, pud, personPlayer._1), startPos(pud, personPlayer._1))
////
////    new World(players, pud.unit._2.units.filter(!_.isStartLocation)
////                                        .flatMap(unit.Unit.fromPudUnit),
////      new Terrain(pud.tiles, pud.mapSizeX, pud.mapSizeY))
////  }
//}