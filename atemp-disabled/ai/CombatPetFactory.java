package gg.warcraft.legends.api.ai;

import java.util.UUID;

public interface CombatPetFactory {

    UUID create(UUID entityId, UUID ownerId);
}
