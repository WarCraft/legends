package gg.warcraft.legends.api.entity.player.service;

import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import java.util.UUID;

public interface LegendsPlayerCommandService extends PlayerCommandService {

    void addGold(UUID playerId, int amount);

    void removeGold(UUID playerId, int amount);

    void addFavor(UUID playerId, int amount);

    void removeFavor(UUID playerId, int amount);
}
