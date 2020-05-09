package gg.warcraft.legends.api.core.leaderboard.handler

class LeaderboardPublishHandler {

}

/* TODO
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

 */