package gg.warcraft.legends.app.entity.player.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.event.EntityInteractEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.util.StringUtils;

import java.util.UUID;

public class PlayerInspectionHandler {
    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final PlayerCommandService playerCommandService;
    private final StringUtils stringUtils;

    @Inject
    public PlayerInspectionHandler(LegendsPlayerQueryService legendsPlayerQueryService,
                                   PlayerCommandService playerCommandService,
                                   StringUtils stringUtils) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.playerCommandService = playerCommandService;
        this.stringUtils = stringUtils;
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
            String teamPrefix = stringUtils.isVowel(team.charAt(0)) ? "an " : "a ";
            String specialization = inspectedPlayer.getSpecialization().getName();
            String message = "You are looking at " + inspectedPlayer.getName() + ", " +
                    teamPrefix + team + specialization;
            playerCommandService.sendNotification(inspectingPlayerId, message);
        }
    }
}
