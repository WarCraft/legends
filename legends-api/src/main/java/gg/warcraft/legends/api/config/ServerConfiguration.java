package gg.warcraft.legends.api.config;

import java.util.List;
import java.util.Map;

public interface ServerConfiguration {

    List<RankPrefixConfiguration> getRankPrefixes();

    List<String> getHumanRankSuffixes();

    List<String> getOrcRankSuffixes();

    List<Integer> getRankFavorTable();

    String getPrestigeChar();

    Map<String, Integer> getObjectivePoints();
}
