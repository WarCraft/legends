package gg.warcraft.legends.app.entity.player.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.LegendsCurrency;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.util.ColorCode;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;

public class GoldCommandHandler implements CommandHandler {
    private static final float TRADE_DISTANCE = 3;

    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;

    @Inject
    public GoldCommandHandler(LegendsPlayerQueryService playerQueryService,
                              LegendsPlayerCommandService playerCommandService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
    }

    boolean printGold(LegendsPlayer player) {
        int gold = player.getGold();
        playerCommandService.sendMessage(player.getId(), ColorCode.YELLOW + "You have " + gold + " gold.");
        return true;
    }

    boolean giveGold(LegendsPlayer player, List<String> arguments) {
        if (arguments.size() != 2) {
            playerCommandService.sendNotification(player.getId(), "Usage: /gold give <name> <amount>");
            return false;
        }

        String targetPlayerName = arguments.get(0);
        UUID targetPlayerId = playerQueryService.resolvePlayerId(targetPlayerName);
        if (targetPlayerId == null) {
            playerCommandService.sendNotification(player.getId(), "There appears to be no player named " +
                    targetPlayerName);
            return false;
        }

        int goldToGive;
        try {
            goldToGive = Integer.parseInt(arguments.get(1));
        } catch (Exception ex) {
            playerCommandService.sendNotification(player.getId(), "That is not a valid amount of gold.");
            return false;
        }

        int playerGold = player.getGold();
        if (goldToGive > playerGold) {
            playerCommandService.sendNotification(player.getId(), "You don't have that much gold.");
            return false;
        }

        LegendsPlayer targetPlayer = playerQueryService.getPlayer(targetPlayerId);
        Vector3f playerLocation = player.getLocation().toVector();
        Vector3f targetPlayerLocation = targetPlayer.getLocation().toVector();
        if (playerLocation.distance(targetPlayerLocation) > TRADE_DISTANCE) {
            playerCommandService.sendNotification(player.getId(), "You need to be closer to " + targetPlayerName + "" +
                    " to give them gold.");
            return false;
        }

        playerCommandService.removeCurrency(player.getId(), LegendsCurrency.GOLD, goldToGive);
        playerCommandService.addCurrency(targetPlayerId, LegendsCurrency.GOLD, goldToGive);
        playerCommandService.sendNotification(player.getId(), "You've given " + goldToGive + " to " + targetPlayerName);
        playerCommandService.sendNotification(targetPlayerId, "You received " + goldToGive + " from " + player.getName());
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        LegendsPlayer player = playerQueryService.getPlayer(sender.getPlayerId());
        if (arguments.isEmpty()) {
            return printGold(player);
        } else {
            switch (arguments.get(0)) {
                case "give":
                    return giveGold(player, arguments.subList(1, arguments.size()));
                default:

                    return true;
            }
        }
    }
}
