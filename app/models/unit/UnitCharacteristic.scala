package models.unit

import format.pud.{Land, CanTarget, MouseBtnAction, none}

class PudUnitCharacteristic(val basic: BasicParams, val attack: AttackParams, val ui: UiParams, val build: BuildParams)

class UnitCharacteristic(val pudUc: PudUnitCharacteristic, val moveSpeed: Int)
    extends PudUnitCharacteristic(pudUc.basic, pudUc.attack, pudUc.ui, pudUc.build) {
  val ticksToMove: Int = 16
}

object UnitCharacteristic {
  def buildLandBuilding(sightRange: Int, hitPoints: Int, unitSize: (Int, Int), priority: Int, points: Int,
                    boxSize: (Int, Int), buildParams: BuildParams) = new UnitCharacteristic(new PudUnitCharacteristic(
       BasicParams(sightRange, hitPoints, 20, unitSize, priority, points, armorUpgradable = false, Land),
       AttackParams.none,
       UiParams(selectableViaRectangle = false, boxSize, none, None),
       buildParams
    ), 0)

  def buildLandMelee(hitPoints: Int, armor: Int, priority: Int, pointForKilling: Int, moveSpeed: Int,
            basicDamage: Int, piercingDamage: Int,
            boxSize: (Int, Int), secondMouseBtn: Option[MouseBtnAction],
            buildTime: Int, goldCost: Int, lumberCost: Int) = new UnitCharacteristic(new PudUnitCharacteristic(
    BasicParams(4, hitPoints, armor, (1, 1), priority, pointForKilling, armorUpgradable = true, Land),
    AttackParams(basicDamage, piercingDamage, weaponsUpgradable = true, new CanTarget(1), 1),
    UiParams(selectableViaRectangle = true, boxSize, none, secondMouseBtn),
    BuildParams(buildTime, goldCost, lumberCost, 0)), moveSpeed)
}