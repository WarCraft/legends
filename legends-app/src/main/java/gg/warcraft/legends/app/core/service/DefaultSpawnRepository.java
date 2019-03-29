package gg.warcraft.legends.app.core.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.service.SpawnRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class DefaultSpawnRepository implements SpawnRepository {
    private final Map<LegendsTeam, OrientedLocation> spawnLocations;

    public DefaultSpawnRepository() {
        this.spawnLocations = new HashMap<>();
    }

    @Override
    public OrientedLocation getSpawn(LegendsTeam team) {
        return spawnLocations.get(team);
    }

    @Override
    public void save(LegendsTeam team, OrientedLocation spawn) {
        spawnLocations.put(team, spawn);
    }
}
