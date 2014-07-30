package models.unit

import format.pud.{MouseBtnAction, Missile}

class UiParams(
    val selectableViaRectangle: Boolean,
    val boxSize: (Int, Int),
    val missileWeapon: Missile,
    val secondMouseBtnAction: Option[MouseBtnAction]
)