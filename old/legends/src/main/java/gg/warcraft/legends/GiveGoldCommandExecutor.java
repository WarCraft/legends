package gg.warcraft.legends;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveGoldCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /givegold <target player name> <amount of gold>");
            return true;
        }

        LegendsPlayer sendingPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(((Player) sender).getUniqueId());
        String targetName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.YELLOW + "Could not find player with name " + targetName);
            return true;
        } else if (targetPlayer.getLocation().distance(sendingPlayer.getLocation()) > 2) {
            sender.sendMessage(ChatColor.YELLOW + "You need to be within 2 blocks of " + targetName + " to give them gold.");
            return true;
        }

        int gold;
        try {
            gold = Integer.parseInt(args[1]);
        } catch (Exception ex) {
            try {
                Double.parseDouble(args[1]);
                sender.sendMessage(ChatColor.YELLOW + "You can only send round numbers of gold.");
                return true;
            } catch (Exception eX) {
                sender.sendMessage(ChatColor.YELLOW + args[1] + " is not a valid amount of gold.");
                return true;
            }
        }

        if (gold > sendingPlayer.getInGameCurrency()) {
            sender.sendMessage(ChatColor.YELLOW + "Whoops you don't have that much gold to give!");
            return true;
        }

        LegendsPlayer targetLegendsPlayer = LegendsPlayerService.getInstance().getLegendsPlayer(targetPlayer.getUniqueId());
        sendingPlayer.removeInGameCurrency(gold);
        sendingPlayer.sendMessage(ChatColor.YELLOW + "You sent " + gold + " gold to " + targetLegendsPlayer.getName());
        targetLegendsPlayer.addInGameCurrency(gold);
        targetLegendsPlayer.sendMessage(ChatColor.YELLOW + "You received " + gold + " gold from " + sendingPlayer.getName());
        return true;
    }
}
