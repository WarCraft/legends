package gg.warcraft.legends.player;

import gg.warcraft.monolith.LoggerService;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.player.DefaultPlayerService;
import gg.warcraft.monolith.player.PlayerRepository;
import org.bukkit.Server;

import java.util.UUID;

public class DefaultLegendsPlayerService extends DefaultPlayerService implements LegendsPlayerService {

    public DefaultLegendsPlayerService(Server server, LoggerService loggerService, PlayerRepository<LegendsPlayer> playerRepository) {
        super(server, loggerService, playerRepository);
    }

    @Override
    public LegendsPlayer getLegendsPlayer(UUID uniqueId) {
        return (LegendsPlayer) getMonolithPlayer(uniqueId);
    }
}
