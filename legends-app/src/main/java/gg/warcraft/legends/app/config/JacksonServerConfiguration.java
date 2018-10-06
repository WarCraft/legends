package gg.warcraft.legends.app.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.item.crate.config.LootCrateConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonServerConfiguration implements ServerConfiguration {
    private final List<RankPrefixConfiguration> rankPrefixes;
    private final List<String> humanRankSuffixes;
    private final List<String> orcRankSuffixes;
    private final List<Integer> rankFavorTable;
    private final String prestigeChar;
    private final Map<String, Integer> objectivePoints;
    private final List<LootCrateConfiguration> lootCrates;

    @JsonCreator
    public JacksonServerConfiguration(@JsonProperty("prestigeChar") String prestigeChar,
                                      @JsonProperty("rankPrefixes") List<RankPrefixConfiguration> rankPrefixes,
                                      @JsonProperty("humanRankSuffixes") List<String> humanRankSuffixes,
                                      @JsonProperty("orcRankSuffixes") List<String> orcRankSuffixes,
                                      @JsonProperty("rankFavorTable") List<Integer> rankFavorTable,
                                      @JsonProperty("objectivePoints") Map<String, Integer> objectivePoints,
                                      @JsonProperty("lootCrates") List<LootCrateConfiguration> lootCrates) {
        this.prestigeChar = prestigeChar;
        this.rankPrefixes = rankPrefixes;
        this.humanRankSuffixes = humanRankSuffixes;
        this.orcRankSuffixes = orcRankSuffixes;
        this.rankFavorTable = rankFavorTable;
        this.objectivePoints = objectivePoints;
        this.lootCrates = lootCrates;
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

    @Override
    public String getPrestigeChar() {
        return prestigeChar;
    }

    @Override
    public Map<String, Integer> getObjectivePoints() {
        return new HashMap<>(objectivePoints);
    }

    @Override
    public List<LootCrateConfiguration> getLootCrates() {
        return new ArrayList<>(lootCrates);
    }
}
