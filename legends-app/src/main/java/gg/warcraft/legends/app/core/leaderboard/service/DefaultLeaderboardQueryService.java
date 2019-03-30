package gg.warcraft.legends.app.core.leaderboard.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.leaderboard.service.AbstractLeaderboardService;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.core.PersistenceService;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

public class DefaultLeaderboardQueryService extends AbstractLeaderboardService implements LeaderboardQueryService {
    private final PersistenceService persistenceService;

    @Inject
    public DefaultLeaderboardQueryService(GameQueryService gameQueryService,
                                          PersistenceService persistenceService) {
        super(gameQueryService);
        this.persistenceService = persistenceService;
    }

    @Override
    public Map<UUID, Integer> getLeaders(LegendsStatistic statistic, int count) {
        String leaderboardKey = getLeaderboardKey(statistic);
        SortedMap<String, Double> leaders = persistenceService.getSortedSet(leaderboardKey, 0, count);
        if (leaders == null || leaders.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<UUID, Integer> result = new LinkedHashMap<>();
        leaders.entrySet().forEach(leader -> {
            UUID leaderId = UUID.fromString(leader.getKey());
            result.put(leaderId, leader.getValue().intValue());
        });
        return result;
    }
}
