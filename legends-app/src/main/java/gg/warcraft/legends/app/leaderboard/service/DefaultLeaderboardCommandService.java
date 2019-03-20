package gg.warcraft.legends.app.leaderboard.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.leaderboard.Leaderboard;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardCommandService;
import gg.warcraft.monolith.api.core.PersistenceService;

import java.util.UUID;

public class DefaultLeaderboardCommandService implements LeaderboardCommandService {
    private final PersistenceService persistenceService;

    @Inject
    public DefaultLeaderboardCommandService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void updateEntry(Leaderboard leaderboard, UUID playerId, int value) {
        persistenceService.setSortedSet(leaderboard.getKey(), playerId.toString(), value);
    }
}
