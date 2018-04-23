package gg.warcraft.legends.player;

import gg.warcraft.monolith.player.PlayerRepository;

import java.util.UUID;

public class DummyPlayerRepository implements PlayerRepository<LegendsPlayer> {

    @Override
    public LegendsPlayer get(UUID uniqueId) {
        return new SimpleLegendsPlayer(uniqueId, "Billy");
    }

    @Override
    public LegendsPlayer getOrNew(UUID uniqueId, String name) {
        return new SimpleLegendsPlayer(uniqueId, name);
    }

    @Override
    public void save(LegendsPlayer player) {

    }
}
