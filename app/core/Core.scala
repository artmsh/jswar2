package core

import util.Random
import collection.mutable
import models.format.{PlayerTypes, PudCodec}
import PudCodec.Pud

class Core(race: Option[Race.Value], resources: Option[Resources.Value], unitSetting: Option[Units.Value],
           opponents: Option[Opponents.Value], tileset: Option[Tileset.Value], pud: Pud) {

  val startPositions = (for {
      unit <- pud.units
      if (unit.Type == 0x5e || unit.Type == 0x5f)
    } yield unit.player.toInt -> (unit.x.toInt, unit.y.toInt)).foldLeft(Map[Int, (Int, Int)]()) { _ + _ }

  val currentPlayer: Player = pickCurrentPlayer
  val enemyPlayers: List[Player] = collectPlayers(List(), currentPlayer, 0, opponents match {
    case Some(p) => p.id + 1 - pud.players.count(_ == PlayerTypes.PlayerComputer)
    case None => 0
  })

  val units: mutable.Seq[Unit] = for {
    rawUnit <- pud.units.toArray
    player <- currentPlayer :: enemyPlayers
    if (rawUnit.player == player.number)
  } yield new Unit(rawUnit, player)

  val terrain: Terrain = new Terrain(pud.tiles, pud.mapSizeX, pud.mapSizeY, units.filter(_.player.number == currentPlayer.number))

  def pickCurrentPlayer = {
    val slotPlayers = (0 until pud.players.length).zip(pud.players).filter(_._2 == PlayerTypes.PlayerPerson)
    val pickedPlayer = slotPlayers(Random.nextInt(slotPlayers.length))

    new Player(PlayerTypes.PlayerPerson, pickedPlayer._1,
      race.getOrElse(pud.races(pickedPlayer._1)), null, startingResourcesAmount(pickedPlayer._1), startPositions(pickedPlayer._1))
  }

  def startingResourcesAmount(index: Int) = {
    Resources.getResourcesAmount(resources, (pud.startGold(index), pud.startLumber(index), pud.startOil(index)))
  }

  def collectPlayers(l: List[Player], currentPlayer: Player, index: Int, count: Int): List[Player] = {
    if (count > 0) {
      if (currentPlayer.number != index) {
        val player: Player = new Player(PlayerTypes.PlayerComputer, index, pud.races(index), pud.aiType(index),
          (pud.startGold(index), pud.startLumber(index), pud.startOil(index)), startPositions(index))
        collectPlayers(player :: l, currentPlayer, index + 1, count - 1)
      } else collectPlayers(l, currentPlayer, index + 1, count)
    } else Nil
  }

  def getData: StartingData = {
    new StartingData(currentPlayer, units.filter(terrain.isSeen).toList, tileset.getOrElse(pud.tileset), terrain.height,
      terrain.width, from2dto1d[Tile](terrain.data, p => terrain.vision(p._1)(p._2) != 0), from2dto1d[Int](terrain.vision, _._3 != 0))
  }

  /** from "[i][j] = val" to (i,j,val) */
  def from2dto1d[T](a: Array[Array[T]], filterFunc: ((Int, Int, T)) => Boolean): List[(Int, Int, T)] = {
    a.zip(0 until a.length)
      .map { p => (p._1.zip(0 until p._1.length) map (row => (p._2, row._2, row._1))) }
      .foldLeft (List[(Int,Int,T)]()) { _ ++ _.filter(filterFunc).toList }
  }
}
