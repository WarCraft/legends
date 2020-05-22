package gg.warcraft.legends.spigot.core.handler;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class CraftPreventionHandler implements Listener {

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractAtEvent(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND
                && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }
}
