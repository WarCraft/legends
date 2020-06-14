/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.config

import gg.warcraft.legends.api.entity.attribute.LegendsAttribute

case class AttributeRollConfig(
    attribute: LegendsAttribute,
    low: Int,
    high: Int
)
