package gg.warcraft.legends.app.entity.player.rank;

import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.legends.api.entity.player.rank.Rank;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SimpleRankTable {
    private final NavigableMap<Integer, Rank> ranks;

    public SimpleRankTable(List<Integer> rankFavorTable, List<RankPrefixConfiguration> rankPrefixes,
                           List<String> rankSuffixes, char prestigeChar) {
        this.ranks = new TreeMap<>();
        Iterator<RankPrefixConfiguration> prefixes = rankPrefixes.iterator();
        RankPrefixConfiguration prefix = prefixes.next();
        Iterator<String> suffixes = rankSuffixes.iterator();
        int totalFavor = 0;
        for (Integer favor : rankFavorTable) {
            if (!suffixes.hasNext()) {
                prefix = prefixes.next();
                suffixes = rankSuffixes.iterator();
            }
            String suffix = suffixes.next();

            String rankName = prefix.getName() + " " + suffix;
            int rankLevel = ranks.size() + 1;
            Rank rank = new SimpleRank(rankName, rankLevel, prefix.getColor());

            totalFavor += favor;
            ranks.put(totalFavor, rank);
        }
    }

    public Rank getRank(int lifetimeFavor) {
        return ranks.floorEntry(lifetimeFavor).getValue();
    }
}
