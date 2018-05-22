package gg.warcraft.legends.app.entity.player.service;

import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerDataRepository;
import gg.warcraft.monolith.api.entity.player.PlayerData;

import java.util.UUID;

public class DefaultLegendsPlayerDataRepository implements LegendsPlayerDataRepository {

    @Override
    public LegendsPlayerData get(UUID playerId) {
        return null;
    }

    @Override
    public void save(PlayerData data) { // FIXME why cant we pass LegendsPlayerData as parameter here?

    }
}
