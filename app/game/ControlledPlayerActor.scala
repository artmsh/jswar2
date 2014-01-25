package game

import akka.actor.Actor
import play.api.libs.json._
import play.api.libs.iteratee.Concurrent.Channel
import models.unit._
import play.api.libs.json.Json.JsValueWrapper
import game.PlayerActor.{Update, Init}
import concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

class ControlledPlayerActor(playerNum: Int, channelFuture: Future[Channel[JsValue]]) extends Actor {
  def receive: Receive = {
    case Init(unitTypes, startPos, race) =>
      channelFuture onSuccess { case channel => channel.push(
        Json.obj(
          "playerNum" -> playerNum,
          "race" -> race,
          "startPosX" -> startPos._1,
          "startPosY" -> startPos._2,
          "unitTypes" -> Json.toJson(unitTypes.toMap))) }

    case Update(updateData) =>
      channelFuture onSuccess { case channel => channel.push(Json.toJson(updateData)) }
  }

  implicit val raceWrites = new Writes[Race] {
    def writes(o: Race): JsValue = o match {
      case Human => JsString("human")
      case Orc => JsString("orc")
      case Neutral => JsString("neutral")
    }
  }

  implicit val kindWrites = new Writes[Kind] {
    def writes(o: Kind): JsValue = Json.toJson(o.getClass.getSimpleName)
  }

  implicit val canTarget = new Writes[CanTarget] {
    def writes(o: CanTarget): JsValue = Json.toJson(o.b)
  }

  implicit val missileWrites = new Writes[Missile] {
    def writes(o: Missile): JsValue = Json.toJson(o.getClass.getSimpleName)
  }

  implicit val mouseBtnAction = new Writes[MouseBtnAction] {
    def writes(o: MouseBtnAction): JsValue = Json.toJson(o.getClass.getSimpleName)
  }

  implicit val ucWrites = new Writes[UnitCharacteristic] {
    def writes(o: UnitCharacteristic): JsValue = {
      var map = Map[String, JsValueWrapper](
      "sightRange" -> o.sightRange,
      "hitPoints" -> o.hitPoints,
      "isMagic" -> o.isMagic,
      "buildTime" -> o.buildTime,
      "goldCost" -> o.goldCost,
      "lumberCost" -> o.lumberCost,
      "oilCost" -> o.oilCost,
      "unitSize" -> Json.toJson(Array(o.unitSize._1, o.unitSize._2)),
      "boxSize" -> Json.toJson(Array(o.boxSize._1, o.boxSize._2)),
      "attackRange" -> o.attackRange,
      "reactionRangeComputer" -> o.reactionRangeComputer,
      "reactionRangeHuman" -> o.reactionRangeHuman,
      "armor" -> o.armor,
      "selectableViaRectangle" -> o.selectableViaRectangle,
      "priority" -> o.priority,
      "basicDamage" -> o.basicDamage,
      "piercingDamage" -> o.piercingDamage,
      "weaponsUpgradable" -> o.weaponsUpgradable,
      "armorUpgradable" -> o.armorUpgradable,
      "kind" -> o.kind,
      "decayRate" -> o.decayRate,
      "annoyFactor" -> o.annoyFactor,
      "pointsForKilling" -> o.pointsForKilling,
      "canTarget" -> o.canTarget,
      "flags" -> o.flags
      )
      o.missileWeapon.foreach(m => map += "missileWeapon" -> m)
      o.secondMouseBtnAction.foreach(m => map += "secondMouseBtnAction" -> m)

      Json.obj(map.toSeq:_*)
    }
  }
}
