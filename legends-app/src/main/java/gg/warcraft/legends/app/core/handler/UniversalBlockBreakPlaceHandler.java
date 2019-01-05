package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.monolith.api.entity.player.GameMode;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.block.event.BlockPreBreakEvent;
import gg.warcraft.monolith.api.world.block.event.BlockPrePlaceEvent;
import gg.warcraft.monolith.api.world.block.event.BlockPreTriggerEvent;

public class UniversalBlockBreakPlaceHandler {
    private final PlayerQueryService playerQueryService;

    @Inject
    public UniversalBlockBreakPlaceHandler(PlayerQueryService playerQueryService) {
        this.playerQueryService = playerQueryService;
    }

    @Subscribe
    public void onBlockPreBreakEvent(BlockPreBreakEvent event) {
        Player player = playerQueryService.getPlayer(event.getPlayerId());
        if (player.getGameMode() != GameMode.CREATIVE) {
            event.cancel();
        }
    }

    @Subscribe
    public void onBlockPrePlaceEvent(BlockPrePlaceEvent event) {
        Player player = playerQueryService.getPlayer(event.getPlayerId());
        if (player.getGameMode() != GameMode.CREATIVE) {
            event.cancel();
        }
    }

    @Subscribe
    public void onBlockPreTriggerEvent(BlockPreTriggerEvent event) {
        if (event.getBlock().getType() == BlockType.FARMLAND) {
            event.cancel();
        }
    }
}
