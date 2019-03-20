package gg.warcraft.legends.app.leaderboard.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.leaderboard.Leaderboard;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.monolith.api.core.PersistenceService;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

public class DefaultLeaderboardQueryService implements LeaderboardQueryService {
    private final PersistenceService persistenceService;

    @Inject
    public DefaultLeaderboardQueryService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public Map.Entry<UUID, Integer> getLeader(Leaderboard leaderboard) {
        SortedMap<String, Double> leader = persistenceService.getSortedSet(leaderboard.getKey(), 0, 1);
        if (leader == null || leader.isEmpty()) {
            return null;
        }

        UUID playerId = UUID.fromString(leader.firstKey());
        int value = Integer.parseInt(leader.get(leader.firstKey()).toString());
        return new HashMap.SimpleEntry<>(playerId, value);
    }
}
