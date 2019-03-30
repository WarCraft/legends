package gg.warcraft.legends.app.core.leaderboard.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.leaderboard.LeaderboardEntry;
import gg.warcraft.legends.api.core.leaderboard.service.AbstractLeaderboardService;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.app.core.leaderboard.SimpleLeaderboardEntry;
import gg.warcraft.monolith.api.core.PersistenceService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public List<LeaderboardEntry> getLeaders(LegendsStatistic statistic, int count) {
        String leaderboardKey = getLeaderboardKey(statistic);
        Map<String, Double> leaders = persistenceService.getSortedSet(leaderboardKey, 0, count);
        if (leaders == null || leaders.isEmpty()) {
            return Collections.emptyList();
        }

        List<LeaderboardEntry> result = new ArrayList<>();
        int rank = 1;
        for (Map.Entry<String, Double> entry : leaders.entrySet()) {
            UUID playerId = UUID.fromString(entry.getKey());
            LeaderboardEntry leaderboardEntry = new SimpleLeaderboardEntry(
                    playerId, rank, entry.getValue().intValue());
            result.add(leaderboardEntry);
            rank += 1;
        }
        return result;
    }
}
