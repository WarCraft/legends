package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

public interface SpawnLocationRepository {

    OrientedLocation getSpawn(LegendsTeam team);

    void save(LegendsTeam team, OrientedLocation spawn);
}
