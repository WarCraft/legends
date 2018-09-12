package gg.warcraft.legends.api.shop;

import gg.warcraft.monolith.api.entity.EntityType;

import java.util.UUID;

public interface MiniPetFactory {

    void spawnPet(EntityType petType, UUID playerId);

    void removePet(UUID playerId);
}
