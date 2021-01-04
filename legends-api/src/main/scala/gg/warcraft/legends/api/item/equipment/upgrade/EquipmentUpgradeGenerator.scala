/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.upgrade

import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.combat.LegendsCombatArchetype._
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.item.{Item, ItemService}

import scala.util.Random

class EquipmentUpgradeGenerator(implicit itemService: ItemService) {
  def generate(archetype: LegendsCombatArchetype, rarity: ItemRarity): Item = {
    val secondaryStat = if (Random.nextBoolean) Crit else Vitality
    val (name, primaryStat) = archetype match {
      case WARRIOR => ("Warrior Scroll", Strength)
      case HUNTER  => ("Hunter Scroll", Dexterity)
      case MAGE    => ("Mage Scroll", Intellect)
    }
    val (primaryAmount, secondaryAmount) = rarity match {
      case ItemRarity.Common    => (1, 1)
      case ItemRarity.Rare      => (2, 1)
      case ItemRarity.Epic      => (3, 2)
      case ItemRarity.Legendary => (5, 3)
    }
    new EquipmentUpgradeBuilder(rarity, name)
      .withAttribute(primaryStat, primaryAmount)
      .withAttribute(secondaryStat, secondaryAmount)
      .build()
  }
}
