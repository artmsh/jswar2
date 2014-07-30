package models.unit

import format.pud.CanTarget

class AttackParams(
    val basicDamage: Int,
    val piercingDamage: Int,
    val weaponsUpgradable: Boolean,
    val canTarget: CanTarget,
    val attackRange: Int
)

object AttackParams {
  def none = new AttackParams(0, 0, false, new CanTarget(0), 0)
}
