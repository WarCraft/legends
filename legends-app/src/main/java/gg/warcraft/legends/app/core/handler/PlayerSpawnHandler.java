package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnLocationRepository;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.event.EntitySpawnEvent;
import gg.warcraft.monolith.api.entity.event.EntityTeamChangedEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Direction;
import gg.warcraft.monolith.api.world.DirectionUtils;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

import java.util.UUID;

public class PlayerSpawnHandler {
    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final EntityCommandService entityCommandService;
    private final SpawnLocationRepository spawnLocationRepository;
    private final DirectionUtils directionUtils;

    @Inject
    public PlayerSpawnHandler(LegendsPlayerQueryService legendsPlayerQueryService,
                              EntityCommandService entityCommandService,
                              SpawnLocationRepository spawnLocationRepository,
                              DirectionUtils directionUtils) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.entityCommandService = entityCommandService;
        this.spawnLocationRepository = spawnLocationRepository;
        this.directionUtils = directionUtils;
    }

    private void teleport(UUID playerId) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
        OrientedLocation spawn = spawnLocationRepository.getSpawn(player.getTeam());
        Direction direction = directionUtils.toDirection(spawn.getYaw());
        entityCommandService.teleport(playerId, spawn, direction);
    }

    @Subscribe
    public void onPlayerRespawnEvent(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            teleport(event.getEntityId());
        }
    }

    @Subscribe
    public void onPlayerTeamChangedEvent(EntityTeamChangedEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            teleport(event.getEntityId());
        }
    }
}
