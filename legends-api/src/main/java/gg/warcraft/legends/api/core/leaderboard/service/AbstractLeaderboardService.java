package gg.warcraft.legends.api.core.leaderboard.service;

import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.util.TimeUtils;

public abstract class AbstractLeaderboardService {
    private static final String DAILY_LEADERBOARD_PREFIX = "leaderboard:daily:";
    private static final String WARRY_LEADERBOARD_PREFIX = "leaderboard:warry:";
    private static final String ALLTIME_LEADERBOARD_PREFIX = "leaderboard:alltime:";

    private final GameQueryService gameQueryService;
    private final TimeUtils timeUtils;

    public AbstractLeaderboardService(GameQueryService gameQueryService,
                                      TimeUtils timeUtils) {
        this.gameQueryService = gameQueryService;
        this.timeUtils = timeUtils;
    }

    protected String getLeaderboardKey(LegendsStatistic statistic) {
        if (statistic.isDaily()) {

            return DAILY_LEADERBOARD_PREFIX + statistic.getKey();
        } else if (statistic.isWarry()) {
            int currentWarId = gameQueryService.getCurrentWar().getId();
            return WARRY_LEADERBOARD_PREFIX + currentWarId + ":" + statistic.getKey();
        } else {
            return ALLTIME_LEADERBOARD_PREFIX + statistic.getKey();
        }
    }
}
