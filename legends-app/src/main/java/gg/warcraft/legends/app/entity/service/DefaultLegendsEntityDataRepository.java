package gg.warcraft.legends.app.entity.service;

import gg.warcraft.legends.api.entity.service.LegendsEntityDataRepository;
import gg.warcraft.monolith.api.entity.EntityData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultLegendsEntityDataRepository implements LegendsEntityDataRepository {
    private final Map<UUID, EntityData> data;

    public DefaultLegendsEntityDataRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public EntityData get(UUID entityId) {
        return data.get(entityId);
    }

    @Override
    public void save(EntityData data) {
        this.data.put(data.getEntityId(), data);
    }
}
