/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

case class Challenge(
    id: String,
    statisticId: String,
    name: String,
    description: String,
    requirement: Int,
    reward: Int
)
