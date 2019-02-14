package gg.warcraft.legends.app.core.handler.statistic;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;

import java.util.UUID;

public class DamageStatisticHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public DamageStatisticHandler(LegendsPlayerQueryService playerQueryService,
                                  LegendsPlayerCommandService playerCommandService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
    }

    private void updateDamageDone(EntityDamageEvent event) {
        UUID entityId = event.getDamage().getSource().getEntityId();
        LegendsPlayer player = playerQueryService.getPlayer(entityId);
        if (player == null) {
            return;
        }

        playerCommandService.increaseStatistics(entityId, (int) event.getDamage().getModifiedValue(),
                LegendsStatistic.DAILY_DAMAGE_DONE, LegendsStatistic.WARRY_DAMAGE_DONE,
                LegendsStatistic.ALLTIME_DAMAGE_DONE);
    }

    private void updateDamageTaken(EntityDamageEvent event) {
        UUID entityId = event.getEntityId();
        LegendsPlayer player = playerQueryService.getPlayer(entityId);
        if (player == null) {
            return;
        }

        playerCommandService.increaseStatistics(entityId, (int) event.getDamage().getModifiedValue(),
                LegendsStatistic.DAILY_DAMAGE_TAKEN, LegendsStatistic.WARRY_DAMAGE_TAKEN,
                LegendsStatistic.ALLTIME_DAMAGE_TAKEN);
    }

    @Subscribe
    public void onEntityDamageEvent(EntityDamageEvent event) {
        updateDamageDone(event);
        updateDamageTaken(event);
    }
}
