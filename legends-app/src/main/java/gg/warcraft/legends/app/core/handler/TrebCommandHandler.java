package gg.warcraft.legends.app.core.handler;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import java.util.List;

public class TrebCommandHandler implements CommandHandler {
    private final PlayerCommandService playerCommandService;

    @Inject
    public TrebCommandHandler(PlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        playerCommandService.sendNotification(sender.getPlayerId(),
                "To start building a trebuchet buy a blueprint from the vendor near your team's spawn!");
        return true;
    }
}
