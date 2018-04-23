package gg.warcraft.legends.player;

import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.player.PlayerService;

import java.util.UUID;

public interface LegendsPlayerService extends PlayerService {

    static LegendsPlayerService getInstance() {
        return (LegendsPlayerService) Monolith.getInstance().getService(Monolith.Service.PLAYER);
    }

    LegendsPlayer getLegendsPlayer(UUID uniqueId);
}
