package gg.warcraft.legends.app.entity.player.statistic.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.event.EntityHealEvent;

import java.util.UUID;

public class DamageHealingDoneTakenStatisticHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public DamageHealingDoneTakenStatisticHandler(LegendsPlayerQueryService playerQueryService,
                                                  LegendsPlayerCommandService playerCommandService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
    }

    private void updateDamageDone(EntityDamageEvent event) {
        UUID entityId = event.getDamage().getSource().getEntityId();
        if (entityId == null) {
            return;
        }

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

    private void updateHealingDone(EntityHealEvent event) {
        UUID entityId = event.getHeal().getSource().getEntityId();
        LegendsPlayer player = playerQueryService.getPlayer(entityId);
        if (player == null) {
            return;
        }

        playerCommandService.increaseStatistics(entityId, (int) event.getHeal().getModifiedValue(),
                LegendsStatistic.DAILY_HEALING_DONE, LegendsStatistic.WARRY_HEALING_DONE,
                LegendsStatistic.ALLTIME_HEALING_DONE);
    }

    private void updateHealingTaken(EntityHealEvent event) {
        UUID entityId = event.getEntityId();
        LegendsPlayer player = playerQueryService.getPlayer(entityId);
        if (player == null) {
            return;
        }

        playerCommandService.increaseStatistics(entityId, (int) event.getHeal().getModifiedValue(),
                LegendsStatistic.DAILY_HEALING_TAKEN, LegendsStatistic.WARRY_HEALING_TAKEN,
                LegendsStatistic.ALLTIME_HEALING_TAKEN);
    }

    @Subscribe
    public void onEntityDamageEvent(EntityDamageEvent event) {
        updateDamageDone(event);
        updateDamageTaken(event);
    }

    @Subscribe
    public void onEntityHealEvent(EntityHealEvent event) {
        updateHealingDone(event);
        updateHealingTaken(event);
    }
}
