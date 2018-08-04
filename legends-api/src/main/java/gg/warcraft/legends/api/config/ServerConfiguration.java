package gg.warcraft.legends.api.config;

import java.util.List;

public interface ServerConfiguration {

    char getPrestigeChar();

    List<RankPrefixConfiguration> getRankPrefixes();

    List<String> getHumanRankSuffixes();

    List<String> getOrcRankSuffixes();

    List<Integer> getRankTable();
}
