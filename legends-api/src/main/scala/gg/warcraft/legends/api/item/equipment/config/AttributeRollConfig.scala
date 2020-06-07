/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.config

import gg.warcraft.monolith.api.entity.attribute.Attribute

case class AttributeRollConfig(
    attribute: Attribute,
    low: Int,
    high: Int
)
