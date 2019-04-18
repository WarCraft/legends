package gg.warcraft.legends.spigot.core.handler;

import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.core.AuthorizationService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.TimeUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;

public class FreezeCommandHandler implements CommandHandler {
    private final AuthorizationService authorizationService;
    private final PlayerCommandService playerCommandService;
    private final EntityCommandService entityCommandService;
    private final TimeUtils timeUtils;
    private final Server server;

    @Inject
    public FreezeCommandHandler(AuthorizationService authorizationService,
                                PlayerCommandService playerCommandService,
                                EntityCommandService entityCommandService,
                                TimeUtils timeUtils, Server server) {
        this.authorizationService = authorizationService;
        this.playerCommandService = playerCommandService;
        this.entityCommandService = entityCommandService;
        this.timeUtils = timeUtils;
        this.server = server;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        } else if (!authorizationService.isMod(sender.getPlayerId())) {
            return false;
        } else if (arguments.isEmpty()) {
            playerCommandService.sendNotification(sender.getPlayerId(), "Please specify who you want to freeze or unfreeze.");
            return true;
        }

        Player target = server.getPlayer(arguments.get(0));
        if (target == null) {
            playerCommandService.sendNotification(sender.getPlayerId(), "Could not find player with name " + arguments.get(0));
            return true;
        } else if (authorizationService.isMod(target.getUniqueId())) {
            playerCommandService.sendNotification(sender.getPlayerId(), "You can't freeze other mods.");
            return true;
        }

        switch (label) {
            case "freeze":
                entityCommandService.freeze(target.getUniqueId(), timeUtils.createDurationInSeconds(900));
                break;
            case "unfreeze":
                entityCommandService.freeze(target.getUniqueId(), timeUtils.createDurationInSeconds(0));
                break;
        }
        return true;
    }
}
