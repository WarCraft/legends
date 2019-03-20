package gg.warcraft.legends.api.core.leaderboard.service;

import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractLeaderboardService {
    private static final String DAILY_LEADERBOARD_PREFIX = "leaderboard:daily:";
    private static final String WARRY_LEADERBOARD_PREFIX = "leaderboard:warry:";
    private static final String ALLTIME_LEADERBOARD_PREFIX = "leaderboard:alltime:";

    private final GameQueryService gameQueryService;
    private final DateTimeFormatter dateTimeFormatter;

    public AbstractLeaderboardService(GameQueryService gameQueryService) {
        this.gameQueryService = gameQueryService;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    }

    private String getCurrentNewYorkDate() {
        LocalDateTime currentUtcTime = LocalDateTime.now();
        LocalDateTime currentNewYorkTime = currentUtcTime.minusHours(5);
        return currentNewYorkTime.toLocalDate().format(dateTimeFormatter);
    }

    protected String getLeaderboardKey(LegendsStatistic statistic) {
        if (statistic.isDaily()) {
            String today = getCurrentNewYorkDate();
            return DAILY_LEADERBOARD_PREFIX + today + ":" + statistic.getKey();
        } else if (statistic.isWarry()) {
            int currentWarId = gameQueryService.getCurrentWar().getId();
            return WARRY_LEADERBOARD_PREFIX + currentWarId + ":" + statistic.getKey();
        } else {
            return ALLTIME_LEADERBOARD_PREFIX + statistic.getKey();
        }
    }
}
