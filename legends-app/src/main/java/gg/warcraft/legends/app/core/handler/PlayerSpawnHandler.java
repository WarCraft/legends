package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnRepository;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.event.EntitySpawnEvent;
import gg.warcraft.monolith.api.entity.event.EntityTeamChangedEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Direction;
import gg.warcraft.monolith.api.world.DirectionUtils;
import gg.warcraft.monolith.api.world.WorldType;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

import java.util.UUID;

public class PlayerSpawnHandler {
    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final EntityCommandService entityCommandService;
    private final SpawnRepository spawnRepository;
    private final DirectionUtils directionUtils;

    @Inject
    public PlayerSpawnHandler(LegendsPlayerQueryService legendsPlayerQueryService,
                              EntityCommandService entityCommandService,
                              SpawnRepository spawnRepository,
                              DirectionUtils directionUtils) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.entityCommandService = entityCommandService;
        this.spawnRepository = spawnRepository;
        this.directionUtils = directionUtils;
    }

    private void teleport(UUID playerId) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
        OrientedLocation spawn = spawnRepository.getSpawn(player.getTeam());
        Direction direction = directionUtils.toDirection(spawn.getYaw());
        entityCommandService.teleport(playerId, spawn, direction);
    }

    @Subscribe
    public void onPlayerRespawnEvent(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getEntityId());
            if (player.getLocation().getWorld().getType() == WorldType.OVERWORLD) {
                teleport(event.getEntityId());
            }
        }
    }

    @Subscribe
    public void onPlayerTeamChangedEvent(EntityTeamChangedEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getEntityId());
            if (player.getLocation().getWorld().getType() == WorldType.OVERWORLD) {
                teleport(event.getEntityId());
            }
        }
    }
}
