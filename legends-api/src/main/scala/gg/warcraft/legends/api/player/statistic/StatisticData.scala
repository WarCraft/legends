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
    allTimeKills: Long = 0,
    allTimeDamage: Long = 0,
    allTimeForts: Long = 0,
    allTimeStrongholds: Long = 0,
    allTimeGold: Long = 0,
    allTimeFavor: Long = 0,
    allTimePlaytime: Long = 0,
    allTimeMobs: Long = 0,
    allTimeResources: Long = 0,
    allTimeCrates: Long = 0
)
