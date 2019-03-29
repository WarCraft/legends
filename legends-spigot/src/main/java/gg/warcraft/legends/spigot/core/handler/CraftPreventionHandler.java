package gg.warcraft.legends.spigot.core.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftPreventionHandler implements Listener {

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent event) {
        event.setCancelled(true);
    }
}
