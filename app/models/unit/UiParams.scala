package models.unit

import format.pud.{MouseBtnAction, Missile}

case class UiParams(
    selectableViaRectangle: Boolean,
    boxSize: (Int, Int),
    missileWeapon: Missile,
    secondMouseBtnAction: Option[MouseBtnAction]
)