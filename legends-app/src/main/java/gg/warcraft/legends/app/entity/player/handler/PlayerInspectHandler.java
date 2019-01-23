package gg.warcraft.legends.app.entity.player.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.event.EntityInteractEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import java.util.UUID;

public class PlayerInspectHandler {
    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final PlayerCommandService playerCommandService;

    @Inject
    public PlayerInspectHandler(LegendsPlayerQueryService legendsPlayerQueryService,
                                PlayerCommandService playerCommandService) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.playerCommandService = playerCommandService;
    }

    @Subscribe
    public void onEntityInteractEvent(EntityInteractEvent event) {
        UUID inspectedPlayerId = event.getEntityId();
        LegendsPlayer inspectedPlayer = legendsPlayerQueryService.getPlayer(inspectedPlayerId);
        if (inspectedPlayer == null) {
            return;
        }

        UUID inspectingPlayerId = event.getPlayerId();
        LegendsPlayer inspectingPlayer = legendsPlayerQueryService.getPlayer(inspectingPlayerId);
        if (inspectingPlayer.isSneaking()) {
            String team = inspectedPlayer.getTeam().getName();
            String specialization = inspectedPlayer.getSpecialization().getName();
            int level = inspectedPlayer.getRank().getLevel();
            String message = "You are looking at " + inspectedPlayer.getName() + ", a level " +
                    level + " " + team + " " + specialization;
            playerCommandService.sendNotification(inspectingPlayerId, message);
        }
    }
}
