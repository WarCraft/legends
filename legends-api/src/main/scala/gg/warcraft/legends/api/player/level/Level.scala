/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.level

import gg.warcraft.monolith.api.core.ColorCode

case class Level(
    value: Int,
    color: ColorCode,
    requiredFavor: Int
    // TODO rewards: List[Unit]
)
