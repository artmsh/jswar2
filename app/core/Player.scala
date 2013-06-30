package core

import Race._
import models.format.{AiType, PlayerType}
import AiType._
import models.format.PlayerType

class Player(val p: PlayerType.Value, val number: Int, val race: Race, val aiType: AiType, val startResources: (Int, Int, Int),
              val startPos: (Int, Int)) {
  // todo upgrades

}
