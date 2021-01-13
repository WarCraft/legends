/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game

import gg.warcraft.monolith.api.util.types.MonolithDate

case class DailyPointsArchive(
    gameId: Long,
    date: MonolithDate,
    dailyPoints: String
)
