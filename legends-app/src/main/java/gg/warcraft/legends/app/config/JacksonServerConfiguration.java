package gg.warcraft.legends.app.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.legends.api.config.ServerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class JacksonServerConfiguration implements ServerConfiguration {
    private final String prestigeChar;
    private final List<RankPrefixConfiguration> rankPrefixes;
    private final List<String> humanRankSuffixes;
    private final List<String> orcRankSuffixes;
    private final List<Integer> rankFavorTable;

    @JsonCreator
    public JacksonServerConfiguration(@JsonProperty("prestigeChar") String prestigeChar,
                                      @JsonProperty("rankPrefixes") List<RankPrefixConfiguration> rankPrefixes,
                                      @JsonProperty("humanRankSuffixes") List<String> humanRankSuffixes,
                                      @JsonProperty("orcRankSuffixes") List<String> orcRankSuffixes,
                                      @JsonProperty("rankFavorTable") List<Integer> rankFavorTable) {
        this.prestigeChar = prestigeChar;
        this.rankPrefixes = rankPrefixes;
        this.humanRankSuffixes = humanRankSuffixes;
        this.orcRankSuffixes = orcRankSuffixes;
        this.rankFavorTable = rankFavorTable;
    }

    @Override
    public String getPrestigeChar() {
        return prestigeChar;
    }

    @Override
    public List<RankPrefixConfiguration> getRankPrefixes() {
        return new ArrayList<>(rankPrefixes);
    }

    @Override
    public List<String> getHumanRankSuffixes() {
        return new ArrayList<>(humanRankSuffixes);
    }

    @Override
    public List<String> getOrcRankSuffixes() {
        return new ArrayList<>(orcRankSuffixes);
    }

    @Override
    public List<Integer> getRankFavorTable() {
        return new ArrayList<>(rankFavorTable);
    }
}
