package gg.warcraft.legends.app.core.leaderboard.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardCommandService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.entity.player.event.PlayerStatisticChangedEvent;

public class LeaderboardUpdateHandler {
    private final LeaderboardCommandService leaderboardCommandService;

    @Inject
    public LeaderboardUpdateHandler(LeaderboardCommandService leaderboardCommandService) {
        this.leaderboardCommandService = leaderboardCommandService;
    }

    @Subscribe
    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {
        if (event.getStatistic() instanceof LegendsStatistic) {
            LegendsStatistic statistic = (LegendsStatistic) event.getStatistic();
            leaderboardCommandService.updateEntry(statistic, event.getPlayerId(), event.getNewCurrent());
        }
    }
}
