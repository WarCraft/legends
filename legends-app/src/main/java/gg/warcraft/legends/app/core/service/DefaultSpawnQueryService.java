package gg.warcraft.legends.app.core.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnQueryService;
import gg.warcraft.legends.api.core.service.SpawnRepository;

import java.util.UUID;

public class DefaultSpawnQueryService implements SpawnQueryService {
    private final SpawnRepository spawnRepository;

    @Inject
    public DefaultSpawnQueryService(SpawnRepository spawnRepository) {
        this.spawnRepository = spawnRepository;
    }

    @Override
    public long getCooldown(UUID playerId) {
        return spawnRepository.getCooldown(playerId);
    }
}
