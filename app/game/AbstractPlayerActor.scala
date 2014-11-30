package game

import akka.actor.Actor
import game.unit.{Change, TerrainAdd, UnitAdd, ResourcesChange}
import game.world.{TileVisibility, Player}

trait AbstractPlayerActor extends Actor {
  val player: Player

  def updateForFirstTime(terrain: Game#Terrain): List[Change] = {
    List(ResourcesChange(player, player.gold, player.lumber, player.oil)) ++
      (player.units map { UnitAdd }) ++
      (player.seenPositions map { v: TileVisibility => TerrainAdd(player, terrain(v.y)(v.x), v) })
  }

  def playerFilter(changes: List[Change]): List[Change] = changes.filter(_.isAffectPlayer(player))
}
