package gg.warcraft.legends.spigot.OLD.world;

import gg.warcraft.legends.world.menu.HologramMenu;
import gg.warcraft.monolith.Staff;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HologramPlacerListener implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasBlock() &&
                event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                player.getInventory().getItemInMainHand().getType() == Material.ARMOR_STAND &&
                Staff.isModerating(player)) {
            RunnableUtils.runOnNextTick(() -> player.openInventory(new HologramMenu(player, event.getClickedBlock())));
            event.setCancelled(true);
        }
    }
}
