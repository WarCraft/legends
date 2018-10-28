package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankCommandService;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.app.entity.player.rank.SimpleRank;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class DefaultRankCommandService implements RankCommandService {
    private final RankRepository rankRepository;

    @Inject
    public DefaultRankCommandService(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }

    @Override
    public void initializeRanks(Team team, List<Integer> rankFavorTable,
                                List<RankPrefixConfiguration> rankPrefixes, List<String> rankSuffixes) {
        NavigableMap<Integer, Rank> ranks = new TreeMap<>();
        Iterator<RankPrefixConfiguration> prefixes = rankPrefixes.iterator();
        RankPrefixConfiguration prefix = prefixes.next();
        Iterator<String> suffixes = rankSuffixes.iterator();
        for (Integer favor : rankFavorTable) {
            if (!suffixes.hasNext()) {
                if (prefixes.hasNext()) {
                    prefix = prefixes.next();
                }
                suffixes = rankSuffixes.iterator();
            }
            String suffix = suffixes.next();

            String rankName = prefix.getName() + " " + suffix;
            int rankLevel = ranks.size() + 1;
            Rank rank = new SimpleRank(rankName, rankLevel, 0, prefix.getColor());

            ranks.put(favor, rank);
        }

        rankRepository.save(team, ranks);

        int maxFavor = rankFavorTable.get(rankFavorTable.size() - 1);
        rankRepository.save(maxFavor);
    }
}
