/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.calendar

import gg.warcraft.monolith.api.util.types.MonolithDate

import java.util.UUID

case class LoginCalendar(
    playerId: UUID,
    day: Int,
    claimed: MonolithDate
)
