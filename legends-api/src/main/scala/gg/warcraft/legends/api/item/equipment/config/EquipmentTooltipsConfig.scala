/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.config

import gg.warcraft.legends.api.item.ItemRarity

case class EquipmentTooltipsConfig(
    default: EquipmentTooltipConfig,
    special: Map[ItemRarity, EquipmentTooltipConfig]
)
