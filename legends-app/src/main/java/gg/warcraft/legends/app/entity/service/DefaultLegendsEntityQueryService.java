package gg.warcraft.legends.app.entity.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.service.LegendsEntityDataRepository;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.EntityData;
import gg.warcraft.monolith.api.entity.EntityServerData;
import gg.warcraft.monolith.api.entity.adapter.EntityServerAdapter;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.app.entity.SimpleEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLegendsEntityQueryService implements EntityQueryService {
    private final LegendsEntityDataRepository repository;
    private final EntityServerAdapter entityServerAdapter;

    @Inject
    public DefaultLegendsEntityQueryService(LegendsEntityDataRepository repository,
                                            EntityServerAdapter entityServerAdapter) {
        this.repository = repository;
        this.entityServerAdapter = entityServerAdapter;
    }

    Entity getEntity(EntityServerData serverData) {
        EntityData data = repository.get(serverData.getEntityId());
        if (data != null) {
            return new SimpleEntity(data, serverData);
        } else {
            EntityData defaultData = null; // TODO impl
            return new SimpleEntity(defaultData, serverData);
        }
    }

    @Override
    public Entity getEntity(UUID entityId) {
        EntityServerData serverData = entityServerAdapter.getEntityServerData(entityId);
        if (serverData != null) {
            return getEntity(serverData);
        }
        return null;
    }

    @Override
    public List<Entity> getNearbyEntities(Location location, float radius) {
        return getNearbyEntities(location, radius, radius, radius);
    }

    @Override
    public List<Entity> getNearbyEntities(Location location, float deltaX, float deltaY, float deltaZ) {
        return entityServerAdapter.getNearbyEntitiesData(location, deltaX, deltaY, deltaZ).stream()
                .map(this::getEntity)
                .collect(Collectors.toList());
    }
}
