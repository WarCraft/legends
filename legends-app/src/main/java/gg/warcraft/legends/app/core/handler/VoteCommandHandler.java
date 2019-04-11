package gg.warcraft.legends.app.core.handler;

import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class VoteCommandHandler implements CommandHandler {
    private final PlayerCommandService playerCommandService;

    @Inject
    public VoteCommandHandler(PlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        playerCommandService.sendNotification(playerId, "https://minecraft-server-list.com/server/423343/vote/");
        playerCommandService.sendNotification(playerId, "https://minecraftservers.org/vote/257786/");
        playerCommandService.sendNotification(playerId, "https://minecraft-server.net/vote/warcraftgg/");
        playerCommandService.sendNotification(playerId, "https://minecraft-mp.com/server/192075/vote/");
        playerCommandService.sendNotification(playerId, "https://minecraftservers.biz/servers/106701/");
        return true;
    }
}
