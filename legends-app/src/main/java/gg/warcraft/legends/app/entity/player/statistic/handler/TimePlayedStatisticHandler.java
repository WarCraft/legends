package gg.warcraft.legends.app.entity.player.statistic.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.monolith.api.entity.player.MonolithStatistic;
import gg.warcraft.monolith.api.entity.player.event.PlayerStatisticChangedEvent;

public class TimePlayedStatisticHandler {
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public TimePlayedStatisticHandler(LegendsPlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @Subscribe
    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {
        if (event.getStatistic() == MonolithStatistic.TIME_PLAYED) {
            playerCommandService.increaseStatistics(event.getPlayerId(), event.getAmount(),
                    LegendsStatistic.DAILY_TIME_PLAYED, LegendsStatistic.WARRY_TIME_PLAYED, LegendsStatistic.ALLTIME_TIME_PLAYED);
        }
    }
}
