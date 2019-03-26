package gg.warcraft.legends.app.entity.player.statistic.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.event.EntityFatalDamageEvent;

import java.util.UUID;

public class KillDeathStatisticHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public KillDeathStatisticHandler(LegendsPlayerQueryService playerQueryService,
                                     LegendsPlayerCommandService playerCommandService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
    }

    @Subscribe
    public void onEntityFatalDamageEvent(EntityFatalDamageEvent event) {
        UUID killedId = event.getEntityId();
        LegendsPlayer killedPlayer = playerQueryService.getPlayer(killedId);
        if (killedPlayer == null) {
            return;
        }

        playerCommandService.increaseStatistics(killedId, 1, LegendsStatistic.DAILY_DEATHS,
                LegendsStatistic.WARRY_DEATHS, LegendsStatistic.ALLTIME_DEATHS);

        UUID attackerId = event.getDamage().getSource().getEntityId();
        if (attackerId == null) {
            return;
        }

        LegendsPlayer attackingPlayer = playerQueryService.getPlayer(attackerId);
        if (attackingPlayer != null) {
            playerCommandService.increaseStatistics(attackerId, 1, LegendsStatistic.DAILY_KILLS,
                    LegendsStatistic.WARRY_KILLS, LegendsStatistic.ALLTIME_KILLS);
        }
    }
}
