package gg.warcraft.legends.api.entity.player.service;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;

import java.util.List;
import java.util.UUID;

public interface LegendsPlayerQueryService extends PlayerQueryService {

    @Override
    LegendsPlayer getPlayer(UUID playerId);

    @Override
    List<LegendsPlayer> getOnlinePlayers();
}
