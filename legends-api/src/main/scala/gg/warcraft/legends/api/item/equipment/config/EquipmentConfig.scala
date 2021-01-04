/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.config

import gg.warcraft.combat.api.combatclass.CombatClass
import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfig.{
  AttributeDistributionConfig, CustomModelDataConfig, EquipmentItemTypeConfig,
  RarityRollConfig
}
import gg.warcraft.legends.api.item.equipment.{AttributeDistribution, EquipmentType}
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.item.ItemTypeOrVariant

import java.util
import scala.util.chaining._

case class EquipmentConfig(
    validTypes: Map[LegendsCombatArchetype, List[EquipmentType]],
    offhandTypes: List[EquipmentType],
    dropRates: RarityRollConfig,
    itemTypes: EquipmentItemTypeConfig,
    attributeRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    legendaryRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    offhandAttributeRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    offhandLegendaryRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    customModelData: Map[EquipmentType, CustomModelDataConfig],
    helmetModelData: Map[CombatClass, CustomModelDataConfig],
    tooltips:
      Map[LegendsCombatArchetype,
        Map[Team,
          Map[AttributeDistribution,
            Map[EquipmentType, EquipmentTooltipsConfig]]
    ]]
) {
  lazy val navigableDropRates: util.NavigableMap[Float, ItemRarity] =
    new util.TreeMap[Float, ItemRarity].tap { map =>
      dropRates.foreach { it => map.put(it._2, it._1) }
    }
}

object EquipmentConfig {
  type RarityRollConfig =
    Map[ItemRarity, Float]
  type EquipmentItemTypeConfig =
    Map[EquipmentType, Map[ItemRarity, ItemTypeOrVariant]]
  type AttributeDistributionConfig =
    Map[AttributeDistribution, List[List[AttributeRollConfig]]]
  type CustomModelDataConfig =
    Map[ItemRarity, Map[Team, Int]]
}
