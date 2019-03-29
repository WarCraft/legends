package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

import java.util.UUID;

public interface SpawnRepository {

    OrientedLocation getSpawn(LegendsTeam team);

    long getCooldown(UUID playerId);

    void save(LegendsTeam team, OrientedLocation spawn);

    void save(long cooldown, UUID playerId);
}
