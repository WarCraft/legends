/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic

import java.util.UUID

case class StatisticData(
    playerId: UUID,
    // dailies
    dailyKills: Long = 0,
    dailyDamage: Long = 0,
    dailyForts: Long = 0,
    dailyStrongholds: Long = 0,
    dailyGold: Long = 0,
    dailyFavor: Long = 0,
    dailyPlaytime: Long = 0,
    dailyMobs: Long = 0,
    dailyResources: Long = 0,
    dailyCrates: Long = 0,
    // monthlies
    monthlyKills: Long = 0,
    monthlyDamage: Long = 0,
    monthlyForts: Long = 0,
    monthlyStrongholds: Long = 0,
    monthlyGold: Long = 0,
    monthlyFavor: Long = 0,
    monthlyPlaytime: Long = 0,
    monthlyMobs: Long = 0,
    monthlyResources: Long = 0,
    monthlyCrates: Long = 0,
    // all-time
    alltimeKills: Long = 0,
    alltimeDamage: Long = 0,
    alltimeForts: Long = 0,
    alltimeStrongholds: Long = 0,
    alltimeGold: Long = 0,
    alltimeFavor: Long = 0,
    alltimePlaytime: Long = 0,
    alltimeMobs: Long = 0,
    alltimeResources: Long = 0,
    alltimeCrates: Long = 0
)
