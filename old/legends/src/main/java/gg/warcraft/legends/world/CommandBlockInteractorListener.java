package gg.warcraft.legends.world;

import gg.warcraft.legends.data.DataService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CommandBlockInteractorListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String command = DataService.getInstance().getCommandBlockCommand(event.getClickedBlock());
            if (command != null &&
                    !(player.getInventory().getItemInMainHand().getType() == Material.COMMAND &&
                            player.getInventory().getItemInOffHand().getType() == Material.FLINT)) {
                // TODO check if whitelisted command
                player.performCommand(command);
                event.setCancelled(true);
            }
        }
    }
}
