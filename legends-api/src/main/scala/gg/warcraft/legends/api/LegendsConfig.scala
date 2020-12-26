/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api

import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.crate.LootCrate
import gg.warcraft.legends.api.player.challenge.Challenge
import gg.warcraft.legends.api.player.level.Level
import gg.warcraft.legends.api.player.statistic.LegendsStatistic.LegendsStatistic
import gg.warcraft.monolith.api.world.Location

case class LegendsConfig(
    spawnLocations: Map[LegendsTeam, Location],
    levels: List[Level],
    objectivePoints: Map[String, Int],
    objectiveCrates: Map[String, Int],
    playerKillFavor: Int,
    mobKillFavor: Int,
    eliteMobKillFavor: Int,
    leaderboards: LeaderboardsConfig,
    lootCrates: List[LootCrate],
    challenges: List[Challenge],
    dailyRewards: List[RewardConfig],
    skirmishTimes: List[Int]
)

case class RewardConfig(
    crate: String,
    count: Int
)

case class LeaderboardsConfig(
    leaderboards: List[LeaderboardConfig],
    dailyPrizes: List[RewardConfig],
    monthlyPrizes: List[RewardConfig]
)

case class LeaderboardConfig(
    statistic: LegendsStatistic,
    name: String,
    description: String
)
