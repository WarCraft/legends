package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.legends.api.entity.player.rank.service.RankRepository;
import gg.warcraft.legends.app.entity.player.rank.SimpleRank;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.NavigableMap;

public class DefaultRankQueryService implements RankQueryService {
    private final String WAYFARER_NAME = "Wayfarer";
    private final ColorCode WAYFARER_COLOR = ColorCode.DARK_GRAY;

    private final RankRepository rankRepository;

    @Inject
    public DefaultRankQueryService(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }

    @Override
    public Rank getRank(Team team, int favor) {
        NavigableMap<Integer, Rank> ranks = rankRepository.getRanks(team);
        if (ranks == null || ranks.isEmpty()) {
            return new SimpleRank(WAYFARER_NAME, 0, WAYFARER_COLOR);
        }

        int maxFavor = rankRepository.getMaxFavor();
        int adjustedFavor = favor % maxFavor;
        Rank rank = ranks.floorEntry(adjustedFavor).getValue();
        int prestige = favor / maxFavor;
        return new SimpleRank(rank.getName(), prestige, rank.getColor());
    }
}
