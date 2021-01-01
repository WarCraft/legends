/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat

import gg.warcraft.combat.api.combatclass.{CombatClass, CombatClassService}

object LegendsCombatClass {
  type Type = String

  final val Berserker: Type = "BERSERKER"
  final val Guardian: Type = "GUARDIAN"
  final val Marksman: Type = "MARKSMAN"
  final val Ranger: Type = "RANGER"
  final val Elementalist: Type = "ELEMENTALIST"
  final val Wizard: Type = "WIZARD"

  @inline implicit final class LegendsCombatClassOps(val self: Type) extends AnyVal {
    def instance(implicit
        combatClassService: CombatClassService
    ): CombatClass = combatClassService.combatClasses(self)
  }
}
