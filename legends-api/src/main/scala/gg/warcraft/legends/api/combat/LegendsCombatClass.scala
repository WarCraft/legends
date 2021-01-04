/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat

import gg.warcraft.combat.api.combatclass.{CombatClass, CombatClassService}

import scala.util.Random

object LegendsCombatClass {
  type Type = String

  final val Berserker: Type = "BERSERKER"
  final val Guardian: Type = "GUARDIAN"
  final val Marksman: Type = "MARKSMAN"
  final val Ranger: Type = "RANGER"
  final val Elementalist: Type = "ELEMENTALIST"
  final val Wizard: Type = "WIZARD"

  def random(implicit
      combatClassService: CombatClassService
  ): CombatClass = Random.nextInt(6) match {
    case 0 => Berserker.instance
    case 1 => Guardian.instance
    case 2 => Marksman.instance
    case 3 => Ranger.instance
    case 4 => Elementalist.instance
    case 5 => Wizard.instance
  }

  @inline implicit final class LegendsCombatClassOps(val self: Type) extends AnyVal {
    def instance(implicit
        combatClassService: CombatClassService
    ): CombatClass = combatClassService.combatClasses(self)
  }
}
