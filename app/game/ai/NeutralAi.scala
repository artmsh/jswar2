package game.ai

import game.world.{Player, UpdateData}

// handle critters only
class NeutralAi(player: Player) extends Ai {
  var critters: List[Unit] = List()

  def update(data: UpdateData) {
    critters :+ (data.addedUnits.filter { case (n, p) => p.name == "unit-critter" } map { case (n, p) => p }).toList
  }
}
