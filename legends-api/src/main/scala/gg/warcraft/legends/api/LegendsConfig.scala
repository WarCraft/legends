/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api

import gg.warcraft.legends.api.item.crate.LootCrate
import gg.warcraft.legends.api.player.challenge.Challenge
import gg.warcraft.legends.api.player.level.Level
import gg.warcraft.legends.api.player.statistic.LegendsStatistic.LegendsStatistic
import gg.warcraft.monolith.api.core.DatabaseConfig
import gg.warcraft.monolith.api.entity.team.Team

case class LegendsConfig(
    // Core
    database: DatabaseConfig,
    // Legends
    challenges: List[Challenge],
    dailyRewards: List[LegendsConfig.RewardConfig],
    leaderboards: LeaderboardsConfig,
    levels: List[Level],
    lootCrates: List[LootCrate.Config],
    // Siege
    objectiveCaptureRewards: Map[String, LegendsConfig.RewardConfig],
    objectiveDefaultOccupiers: Map[String, Team],
    objectiveTallyPoints: Map[String, Int],
    playerKillFavor: Int,
    mobKillFavor: Int,
    eliteMobKillFavor: Int,
    skirmishTimes: List[Int]
)

case class LeaderboardsConfig(
    leaderboards: List[LeaderboardConfig],
    dailyPrizes: List[Map[LootCrate.Id, Int]],
    monthlyPrizes: List[Map[LootCrate.Id, Int]]
)

case class LeaderboardConfig(
    statistic: LegendsStatistic,
    name: String,
    description: String
)

object LegendsConfig {
  type RewardConfig = Map[LootCrate.Id, Int]
}
