/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.statistic.handler

class PlaytimeStatisticHandler {

}


/*
public class TimePlayedStatisticHandler {
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public TimePlayedStatisticHandler(LegendsPlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @Subscribe TODO is this event still fired? Should it be fired from monolith for this stat or should all stats be implemented by server?
    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {
        if (event.getStatistic() == MonolithStatistic.TIME_PLAYED) {
            playerCommandService.increaseStatistics(event.getPlayerId(), event.getAmount(),
                    LegendsStatistic.DAILY_TIME_PLAYED, LegendsStatistic.WARRY_TIME_PLAYED, LegendsStatistic.ALLTIME_TIME_PLAYED);
        }
    }
}

 */