/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.basic

import gg.warcraft.combat.api.combatclass.CombatClass
import gg.warcraft.legends.api.combat.LegendsCombatClass.{
  Berserker, Elementalist, Guardian, Marksman, Ranger, Wizard
}
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute.{
  Dexterity, Intellect, Strength
}
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder
import gg.warcraft.monolith.api.item.ItemType._
import gg.warcraft.monolith.api.item.{Item, ItemService}

class BasicEquipmentFactory {
  def create(combatClass: Option[CombatClass])(implicit
      itemService: ItemService
  ): Option[Item] = {
    val data = combatClass match {
      case Some(combatClass) =>
        combatClass.id match {
          case Berserker    => Some((AXE, "Basic Axe", Strength, 1))
          case Guardian     => Some((SWORD, "Basic Sword", Strength, 1))
          case Marksman     => Some((CROSSBOW, "Basic Crossbow", Dexterity, 100))
          case Ranger       => Some((BOW, "Basic Bow", Dexterity, 100))
          case Elementalist => Some((HOE, "Basic Staff", Intellect, 1))
          case Wizard       => Some((STICK, "Basic Wand", Intellect, 100))
          case _            => None
        }
      case _ => None
    }

    data.map { data =>
      val builder = new EquipmentBuilder(data._1, ItemRarity.Common, data._2)
      builder.addAttribute(data._3, 1)
        .withCustomModelData(data._4)
        .build()
    }
  }
}
