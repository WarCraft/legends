package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.monolith.api.core.AuthorizationService;
import gg.warcraft.monolith.api.entity.player.GameMode;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.block.event.BlockPreBreakEvent;
import gg.warcraft.monolith.api.world.block.event.BlockPrePlaceEvent;
import gg.warcraft.monolith.api.world.block.event.BlockPreTriggerEvent;

public class UniversalBlockBreakPlaceHandler {
    private final PlayerQueryService playerQueryService;
    private final AuthorizationService authorizationService;

    @Inject
    public UniversalBlockBreakPlaceHandler(PlayerQueryService playerQueryService,
                                           AuthorizationService authorizationService) {
        this.playerQueryService = playerQueryService;
        this.authorizationService = authorizationService;
    }

    @Subscribe
    public void onBlockPreBreakEvent(BlockPreBreakEvent event) {
        event.cancel();
        Player player = playerQueryService.getPlayer(event.getPlayerId());
        if (player.getGameMode() == GameMode.CREATIVE
                && authorizationService.isModerating(event.getPlayerId())) {
            event.explicitlyAllow();
        }
    }

    @Subscribe
    public void onBlockPrePlaceEvent(BlockPrePlaceEvent event) {
        event.cancel();
        Player player = playerQueryService.getPlayer(event.getPlayerId());
        if (player.getGameMode() == GameMode.CREATIVE
                && authorizationService.isModerating(event.getPlayerId())) {
            event.explicitlyAllow();
        }
    }

    @Subscribe
    public void onBlockPreTriggerEvent(BlockPreTriggerEvent event) {
        if (event.getBlock().type() == BlockType.FARMLAND) {
            event.cancel();
        }
    }
}
