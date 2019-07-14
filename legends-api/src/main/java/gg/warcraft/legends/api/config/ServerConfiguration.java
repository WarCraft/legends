package gg.warcraft.legends.api.config;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.config.LootCrateConfiguration;
import gg.warcraft.monolith.api.world.location.config.LocationWithOrientationConfig;

import java.util.List;
import java.util.Map;

public interface ServerConfiguration {

    Map<LegendsTeam, LocationWithOrientationConfig> getSpawnLocations();

    List<RankPrefixConfiguration> getRankPrefixes();

    List<String> getHumanRankSuffixes();

    List<String> getOrcRankSuffixes();

    List<Integer> getRankFavorTable();

    Map<String, Integer> getObjectivePoints();

    Map<String, Integer> getObjectiveCrates();

    int getFavorPlayerKill();

    int getFavorEliteMobKill();

    int getFavorMobKill();

    List<LootCrateConfiguration> getLootCrates();

    Map<Integer, Map<LootCrateType, Integer>> getRankUpRewards();
}
