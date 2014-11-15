package game

import akka.actor.Actor
import game.unit.{Change, TerrainAdd, UnitAdd, ResourcesChange}
import game.world.{TileVisibility, Player}

trait AbstractPlayerActor extends Actor {
  val player: Player

  def updateForFirstTime(terrain: Game#Terrain): List[Change] = {
    List(ResourcesChange(player.gold, player.lumber, player.oil)) ++
      (player.units map { UnitAdd }) ++
      (player.seenPositions map { v: TileVisibility => TerrainAdd(terrain(v.y)(v.x), v) })
  }
}
