package core

import utils.{AiType, PlayerTypes}
import Race._
import AiType._

class Player(val p: PlayerTypes.Value, val number: Int, val race: Race, val aiType: AiType, val startResources: (Int, Int, Int),
              val startPos: (Int, Int)) {
  // todo upgrades

}
