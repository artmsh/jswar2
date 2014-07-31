package models.unit

import format.pud.Kind

case class BasicParams(
    sightRange: Long,
    hitPoints: Int,
    armor: Int,
    unitSize: (Int, Int),
    priority: Int,
    pointsForKilling: Int,
    armorUpgradable: Boolean,
    kind: Kind
)
