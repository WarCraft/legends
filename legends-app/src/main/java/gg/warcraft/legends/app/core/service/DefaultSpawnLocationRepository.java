package gg.warcraft.legends.app.core.service;

import gg.warcraft.legends.api.core.service.SpawnLocationRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

import java.util.HashMap;
import java.util.Map;

public class DefaultSpawnLocationRepository implements SpawnLocationRepository {
    private final Map<LegendsTeam, OrientedLocation> spawnLocations;

    public DefaultSpawnLocationRepository() {
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
