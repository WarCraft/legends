package gg.warcraft.legends.app.entity.player.statistic.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.player.event.PlayerCurrencyGainedEvent;

import java.util.UUID;

public class CurrencyStatisticHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public CurrencyStatisticHandler(LegendsPlayerQueryService playerQueryService,
                                    LegendsPlayerCommandService playerCommandService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
    }

    private void updateStatistic(UUID playerId, LegendsCurrency currency, int amount) {
        switch (currency) {
            case GOLD:
                playerCommandService.increaseStatistics(playerId, amount, LegendsStatistic.DAILY_GOLD_GAINED,
                        LegendsStatistic.WARRY_GOLD_GAINED, LegendsStatistic.ALLTIME_GOLD_GAINED);
                break;
            case FAVOR:
                playerCommandService.increaseStatistics(playerId, amount, LegendsStatistic.DAILY_FAVOR_GAINED,
                        LegendsStatistic.WARRY_FAVOR_GAINED, LegendsStatistic.ALLTIME_FAVOR_GAINED);
                break;
        }
    }

    @Subscribe
    public void onPlayerCurrencyGained(PlayerCurrencyGainedEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            return;
        }

        LegendsCurrency currency = LegendsCurrency.valueOf(event.getCurrency().toUpperCase());
        updateStatistic(playerId, currency, event.getAmount());
    }
}
