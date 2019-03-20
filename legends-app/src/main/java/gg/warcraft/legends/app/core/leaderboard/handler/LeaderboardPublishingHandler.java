package gg.warcraft.legends.app.core.leaderboard.handler;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardCommandService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;

import java.util.Arrays;
import java.util.Iterator;

@Singleton
public class LeaderboardPublishingHandler implements Runnable {
    private final LeaderboardCommandService leaderboardCommandService;

    private Iterator<LegendsStatistic> iterator;

    public LeaderboardPublishingHandler(LeaderboardCommandService leaderboardCommandService) {
        this.leaderboardCommandService = leaderboardCommandService;
        this.iterator = newIterator();
    }

    private Iterator<LegendsStatistic> newIterator() {
        return Arrays.asList(LegendsStatistic.values()).iterator();
    }

    @Override
    public void run() {
        if (!iterator.hasNext()) {
            iterator = newIterator();
        }
        LegendsStatistic nextStatistic = iterator.next();
        leaderboardCommandService.publishLeaderboard(nextStatistic);
    }
}
