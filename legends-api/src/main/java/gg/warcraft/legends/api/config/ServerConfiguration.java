package gg.warcraft.legends.api.config;

import java.util.List;

public interface ServerConfiguration {

    String getPrestigeChar();

    List<RankPrefixConfiguration> getRankPrefixes();

    List<String> getHumanRankSuffixes();

    List<String> getOrcRankSuffixes();

    List<Integer> getRankFavorTable();
}
