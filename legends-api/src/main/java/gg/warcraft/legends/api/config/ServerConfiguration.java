package gg.warcraft.legends.api.config;

import gg.warcraft.legends.api.item.crate.config.LootCrateConfiguration;

import java.util.List;
import java.util.Map;

public interface ServerConfiguration {

    List<RankPrefixConfiguration> getRankPrefixes();

    List<String> getHumanRankSuffixes();

    List<String> getOrcRankSuffixes();

    List<Integer> getRankFavorTable();

    String getPrestigeChar();

    Map<String, Integer> getObjectivePoints();

    List<LootCrateConfiguration> getLootCrates();
}
