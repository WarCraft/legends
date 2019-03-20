package gg.warcraft.legends.app.core.leaderboard.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.leaderboard.service.AbstractLeaderboardService;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.List;
import java.util.SortedMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLeaderboardQueryService extends AbstractLeaderboardService implements LeaderboardQueryService {
    private final PersistenceService persistenceService;

    @Inject
    public DefaultLeaderboardQueryService(GameQueryService gameQueryService,
                                          PersistenceService persistenceService,
                                          TimeUtils timeUtils) {
        super(gameQueryService, timeUtils);
        this.persistenceService = persistenceService;
    }

    @Override
    public List<UUID> getLeaders(LegendsStatistic statistic, int count) {
        String leaderboardKey = getLeaderboardKey(statistic);
        SortedMap<String, Double> leaders = persistenceService.getSortedSet(leaderboardKey, 0, count);
        if (leaders == null || leaders.isEmpty()) {
            return null;
        }

        return leaders.entrySet().stream()
                .map(leader -> UUID.fromString(leader.getKey()))
                .collect(Collectors.toList());
    }
}
