package gg.warcraft.legends.api.ai;

import java.util.UUID;

public interface MiniPetFactory {

    UUID create(UUID entityId, UUID ownerId);
}
