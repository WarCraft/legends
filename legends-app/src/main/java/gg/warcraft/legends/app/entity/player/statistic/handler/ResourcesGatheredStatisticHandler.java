package gg.warcraft.legends.app.entity.player.statistic.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.gathering.api.gatherable.event.BlockGatheredEvent;
import gg.warcraft.gathering.api.gatherable.event.EntityGatheredEvent;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;

import java.util.UUID;

public class ResourcesGatheredStatisticHandler {
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public ResourcesGatheredStatisticHandler(LegendsPlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    private void updateResourcesGathered(UUID playerId) {
        if (playerId == null) {
            return;
        }

        playerCommandService.increaseStatistics(playerId, 1, LegendsStatistic.DAILY_RESOURCES_GATHERED,
                LegendsStatistic.WARRY_RESOURCES_GATHERED, LegendsStatistic.ALLTIME_RESOURCES_GATHERED);
    }

    @Subscribe
    public void onBlockGatheredEvent(BlockGatheredEvent event) {
        updateResourcesGathered(event.getPlayerId());
    }

    @Subscribe
    public void onEntityGatheredEvent(EntityGatheredEvent event) {
        updateResourcesGathered(event.getPlayerId());
    }
}
