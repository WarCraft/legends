package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.world.Location;

import java.util.UUID;

public interface SpawnRepository {

    Location getSpawn(LegendsTeam team);

    long getCooldown(UUID playerId);

    void save(LegendsTeam team, Location spawn);

    void save(long cooldown, UUID playerId);
}
