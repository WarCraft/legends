package gg.warcraft.legends.api.entity.service;

import gg.warcraft.monolith.api.entity.EntityData;

import java.util.UUID;

public interface LegendsEntityDataRepository {

    EntityData get(UUID entityId);

    void save(EntityData data);
}
