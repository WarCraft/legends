package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.combat.api.ability.event.AbilityPreHitEvent;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.world.World;

import javax.inject.Inject;

public class TutorialPvpHandler {
    private final PlayerQueryService playerQueryService;

    @Inject
    public TutorialPvpHandler(PlayerQueryService playerQueryService) {
        this.playerQueryService = playerQueryService;
    }

    @Subscribe
    public void onAbilityPreHitEvent(AbilityPreHitEvent event) {
        Player target = playerQueryService.getPlayer(event.getTargetId());
        if (target != null
                && target.getLocation().world() == World.THE_END) {
            event.cancel();
        }
    }
}
