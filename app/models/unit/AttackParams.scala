package models.unit

import format.pud.CanTarget

case class AttackParams(
    basicDamage: Int,
    piercingDamage: Int,
    weaponsUpgradable: Boolean,
    canTarget: CanTarget,
    attackRange: Int
)

object AttackParams {
  def none = AttackParams(0, 0, weaponsUpgradable = false, new CanTarget(0), 0)
}
