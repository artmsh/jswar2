package core

import Race._
import models.{PlayerTypes, AiType}
import AiType._

class Player(val p: PlayerTypes.Value, val number: Int, val race: Race, val aiType: AiType, val startResources: (Int, Int, Int),
              val startPos: (Int, Int)) {
  // todo upgrades

}
