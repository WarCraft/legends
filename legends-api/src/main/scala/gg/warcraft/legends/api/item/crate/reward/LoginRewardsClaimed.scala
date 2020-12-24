/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.crate.reward

import java.time.LocalDate
import java.util.UUID

case class LoginRewardsClaimed(
    playerId: UUID,
    counter: Int = 0,
    date: LocalDate = LocalDate.ofEpochDay(0)
)
