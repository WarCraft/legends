package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.List;

public interface RankCommandService {

    void initializeRanks(Team team, List<Integer> rankFavorTable,
                         List<RankPrefixConfiguration> rankPrefixes, List<String> rankSuffixes);
}
