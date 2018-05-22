package gg.warcraft.legends.api.entity.player.service;

import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.monolith.api.entity.player.PlayerData;
import gg.warcraft.monolith.api.entity.player.service.PlayerDataRepository;

import java.util.UUID;

public interface LegendsPlayerDataRepository extends PlayerDataRepository {

    @Override
    LegendsPlayerData get(UUID playerId);

    @Override
    void save(PlayerData data); // FIXME why cant we pass LegendsPlayerData as parameter here?
}
