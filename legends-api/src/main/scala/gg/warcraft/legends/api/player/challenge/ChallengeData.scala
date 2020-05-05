package gg.warcraft.legends.api.player.challenge

import java.util.UUID

case class ChallengeData(
    playerId: UUID,
    // dailies
    dailyKills: Boolean = false,
    dailyDamage: Boolean = false,
    dailyForts: Boolean = false,
    dailyStrongholds: Boolean = false,
    dailyGold: Boolean = false,
    dailyFavor: Boolean = false,
    dailyPlaytime: Boolean = false,
    dailyMobs: Boolean = false,
    dailyResources: Boolean = false,
    dailyCrates: Boolean = false,
    // monthlies
    monthlyKills: Boolean = false,
    monthlyDamage: Boolean = false,
    monthlyForts: Boolean = false,
    monthlyStrongholds: Boolean = false,
    monthlyGold: Boolean = false,
    monthlyFavor: Boolean = false,
    monthlyPlaytime: Boolean = false,
    monthlyMobs: Boolean = false,
    monthlyResources: Boolean = false,
    monthlyCrates: Boolean = false,
    // all-time
    allTimeKills: Int = 0,
    allTimeDamage: Int = 0,
    allTimeForts: Int = 0,
    allTimeStrongholds: Int = 0,
    allTimeGold: Int = 0,
    allTimeFavor: Int = 0,
    allTimePlaytime: Int = 0,
    allTimeMobs: Int = 0,
    allTimeResources: Int = 0,
    allTimeCrates: Int = 0
)
