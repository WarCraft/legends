/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat

sealed abstract class LegendsCombatClass(
    val name: String,
    val typed: LegendsCombatClass.Type
)

object LegendsCombatClass {
  sealed trait Type
  object Type {
    case object Warrior extends Type
    case object Hunter extends Type
    case object Mage extends Type
  }

  case object Guardian extends LegendsCombatClass("Guardian", Type.Warrior)
  case object Berserker extends LegendsCombatClass("Berserker", Type.Warrior)
  case object Ranger extends LegendsCombatClass("Ranger", Type.Hunter)
  case object Marksman extends LegendsCombatClass("Marksman", Type.Hunter)
  case object Elementalist extends LegendsCombatClass("Elementalist", Type.Mage)
  case object Wizard extends LegendsCombatClass("Wizard", Type.Mage)
}
