package gg.warcraft.legends.app.core.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.service.SpawnRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.world.location.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class DefaultSpawnRepository implements SpawnRepository {
    private final Map<LegendsTeam, Location> spawnLocations;
    private final Map<UUID, Long> cooldowns;

    public DefaultSpawnRepository() {
        this.spawnLocations = new HashMap<>();
        this.cooldowns = new HashMap<>();
    }

    @Override
    public Location getSpawn(LegendsTeam team) {
        return spawnLocations.get(team);
    }

    @Override
    public long getCooldown(UUID playerId) {
        return cooldowns.getOrDefault(playerId, 0L);
    }

    @Override
    public void save(LegendsTeam team, Location spawn) {
        spawnLocations.put(team, spawn);
    }

    @Override
    public void save(long cooldown, UUID playerId) {
        cooldowns.put(playerId, cooldown);
    }
}
