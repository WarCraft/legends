package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.MetricsService;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.event.EntityTeamChangedEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerDisconnectEvent;
import gg.warcraft.monolith.api.entity.team.service.TeamQueryService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerCountMetricHandler implements Runnable {
    private final TeamQueryService teamQueryService;
    private final MetricsService metricsService;
    private final Map<LegendsTeam, Set<UUID>> players;

    @Inject
    public PlayerCountMetricHandler(TeamQueryService teamQueryService, MetricsService metricsService) {
        this.teamQueryService = teamQueryService;
        this.metricsService = metricsService;
        this.players = new HashMap<>();
    }

    @Override
    public void run() {
        players.forEach((team, playerIds) -> metricsService.recordPlayerCount(team, playerIds.size()));
    }

    private void addPlayerTo(UUID playerId, LegendsTeam team) {
        Set<UUID> teamMates = players.computeIfAbsent(team, key -> new HashSet<>());
        teamMates.add(playerId);
    }

    private void removePlayerFrom(UUID playerId, LegendsTeam team) {
        Set<UUID> teamMates = players.computeIfAbsent(team, key -> new HashSet<>());
        teamMates.remove(playerId);
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsTeam team = (LegendsTeam) teamQueryService.getTeam(playerId);
        addPlayerTo(playerId, team);
    }

    @Subscribe
    public void onEntityTeamChangedEvent(EntityTeamChangedEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        UUID playerId = event.getEntityId();
        removePlayerFrom(playerId, (LegendsTeam) event.getPreviousTeam());
        addPlayerTo(playerId, (LegendsTeam) event.getNewTeam());
    }

    @Subscribe
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsTeam team = (LegendsTeam) teamQueryService.getTeam(playerId);
        removePlayerFrom(playerId, team);
    }
}
