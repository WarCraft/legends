package gg.warcraft.legends.api.core.service;

import java.util.UUID;

public interface SpawnQueryService {

    long getCooldown(UUID playerId);
}
