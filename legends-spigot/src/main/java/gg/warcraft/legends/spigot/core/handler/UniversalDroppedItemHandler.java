package gg.warcraft.legends.spigot.core.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UniversalDroppedItemHandler implements Listener {

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        ItemMeta itemMeta = event.getItemDrop().getItemStack().getItemMeta();
        if (itemMeta == null) {
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore.isEmpty()) {
            return;
        }

        String itemType = lore.get(0);
        if (!itemType.contains("Equipment")
                && !itemType.contains("Blueprint")
                && !itemType.contains("Consumable")) {
            event.setCancelled(true);
        }
    }
}
