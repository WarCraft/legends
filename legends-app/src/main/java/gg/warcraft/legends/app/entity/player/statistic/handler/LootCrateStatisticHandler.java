package gg.warcraft.legends.app.entity.player.statistic.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.item.crate.event.LootCrateOpenedEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

public class LootCrateStatisticHandler {
    private final PlayerCommandService playerCommandService;

    @Inject
    public LootCrateStatisticHandler(PlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @Subscribe
    public void onLootCrateOpenedEvent(LootCrateOpenedEvent event) {
        playerCommandService.increaseStatistics(event.getPlayerId(), 1, LegendsStatistic.DAILY_CRATES_OPENED,
                LegendsStatistic.WARRY_CRATES_OPENED, LegendsStatistic.ALLTIME_CRATES_OPENED);
    }
}
