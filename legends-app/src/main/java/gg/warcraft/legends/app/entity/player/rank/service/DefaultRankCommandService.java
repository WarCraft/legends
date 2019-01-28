package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankCommandService;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.app.entity.player.rank.SimpleRank;

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
    public void initializeRanks(LegendsTeam team, List<Integer> rankFavorTable,
                                List<RankPrefixConfiguration> rankPrefixes, List<String> rankSuffixes) {
        NavigableMap<Integer, Rank> ranksByFavor = new TreeMap<>();
        int level = 0;
        int favor = 0;
        int maxFavor = 0;
        for (int prestige = 0; prestige <= 4; prestige += 1) {
            Iterator<RankPrefixConfiguration> prefixes = rankPrefixes.iterator();
            RankPrefixConfiguration prefix = prefixes.next();
            Iterator<String> suffixes = rankSuffixes.iterator();
            int prestigeFavor = prestige * maxFavor;
            for (Integer requiredFavor : rankFavorTable) {
                maxFavor = Math.max(maxFavor, requiredFavor);
                level += 1;
                requiredFavor += prestigeFavor;

                // calculate next rank name
                if (!suffixes.hasNext()) {
                    if (prefixes.hasNext()) {
                        prefix = prefixes.next();
                    }
                    suffixes = rankSuffixes.iterator();
                }
                String suffix = suffixes.next();
                String rankName = prefix.getName() + " " + suffix;

                // create and save rank
                Rank rank = new SimpleRank(rankName, level, prestige, prefix.getColor(), favor, requiredFavor);
                ranksByFavor.put(favor, rank);

                // update rank base favor to previous level required favor
                favor = requiredFavor;
            }
        }

        rankRepository.save(team, ranksByFavor);
    }
}
