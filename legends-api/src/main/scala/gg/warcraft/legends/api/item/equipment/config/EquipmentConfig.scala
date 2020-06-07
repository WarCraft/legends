/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.config

import java.util

import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.{AttributeDistribution, EquipmentType}
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfig.{
  AttributeDistributionConfig, EquipmentItemTypeConfig, RarityRollConfig
}
import gg.warcraft.monolith.api.item.ItemTypeOrVariant

case class EquipmentConfig(
    validTypes: Map[LegendsCombatArchetype, List[EquipmentType]],
    validOffhandTypes: Map[LegendsCombatArchetype, List[EquipmentType]],
    itemRarities: RarityRollConfig,
    offhandTypes: List[EquipmentType],
    itemTypes: EquipmentItemTypeConfig,
    attributeRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    legendaryRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    offhandAttributeRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    offhandLegendaryRolls: Map[LegendsCombatArchetype, AttributeDistributionConfig],
    tooltips:
        Map[LegendsCombatArchetype,
            Map[LegendsTeam,
                Map[AttributeDistribution,
                    Map[EquipmentType,
                        Map[ItemRarity,
                            List[EquipmentTooltipConfig]]]]]]
)

object EquipmentConfig {
  type RarityRollConfig = util.NavigableMap[Float, ItemRarity]
  type EquipmentItemTypeConfig =
    Map[EquipmentType, Map[ItemRarity, ItemTypeOrVariant]]
  type AttributeDistributionConfig =
    Map[AttributeDistribution, List[List[AttributeRollConfig]]]
}
