package gg.warcraft.legends.api.entity.player.service;

import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import java.util.UUID;

public interface LegendsPlayerCommandService extends PlayerCommandService {

    void addGoldCoins(UUID playerId, int amount);

    void subtractGoldCoins(UUID playerId, int amount);

    void addFavor(UUID playerId, int amount);

    void subtractFavor(UUID playerId, int amount);
}
