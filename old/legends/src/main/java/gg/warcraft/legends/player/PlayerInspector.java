package gg.warcraft.legends.player;

import gg.warcraft.monolith.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInspector implements Listener {

    private final LegendsPlayerService playerService;

    public PlayerInspector(LegendsPlayerService playerService) {
        this.playerService = playerService;
    }

    // ignoreCancelled should filter out any shift right clicks executed with a combat weapon
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().isSneaking() && event.getRightClicked().getType() == EntityType.PLAYER) {
            LegendsPlayer inspectedPlayer = playerService.getLegendsPlayer(event.getRightClicked().getUniqueId());
            String playerRace = inspectedPlayer.getRace().name().toLowerCase();
            playerRace = playerRace.substring(0, 1).toUpperCase() + playerRace.substring(1);
            String racePrefix = StringUtils.isVowel(playerRace.charAt(0))
                    ? "an "
                    : "a ";
            String text = "You are looking at " + inspectedPlayer.getDisplayName() + ", " + racePrefix + playerRace +
                    " who is described as " + inspectedPlayer.getDescription() + ".";
            event.getPlayer().sendMessage(ChatColor.YELLOW + text);
        }
    }
}
