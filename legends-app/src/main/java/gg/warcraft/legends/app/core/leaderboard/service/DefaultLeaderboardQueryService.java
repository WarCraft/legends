package gg.warcraft.legends.app.core.leaderboard.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.core.leaderboard.service.AbstractLeaderboardService;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

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
    public Map.Entry<UUID, Integer> getLeader(LegendsStatistic statistic) {
        String leaderboardKey = getLeaderboardKey(statistic);
        SortedMap<String, Double> leader = persistenceService.getSortedSet(leaderboardKey, 0, 1);
        if (leader == null || leader.isEmpty()) {
            return null;
        }

        UUID playerId = UUID.fromString(leader.firstKey());
        int value = Integer.parseInt(leader.get(leader.firstKey()).toString());
        return new HashMap.SimpleEntry<>(playerId, value);
    }
}
