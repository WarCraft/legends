package gg.warcraft.legends.api

import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.crate.config.LootCrateConfiguration
import gg.warcraft.legends.api.item.crate.LootCrateType
import gg.warcraft.legends.api.player.challenge.Challenge
import gg.warcraft.legends.api.player.level.Level
import gg.warcraft.monolith.api.world.Location

case class LegendsConfig(
    spawnLocations: Map[LegendsTeam, Location],
    levels: List[Level],
    objectivePoints: Map[String, Int],
    objectiveCrates: Map[String, Int],
    playerKillFavor: Int,
    mobKillFavor: Int,
    eliteMobKillFavor: Int,
    lootCrates: List[LootCrateConfiguration],
    challenges: List[Challenge],
    skirmishTimes: List[Int]
)
