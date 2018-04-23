package gg.warcraft.legends.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NonCustomItemDropPreventor implements Listener {

    private boolean canBeDropped(ItemStack item) {
        // TODO remove as part of LEGENDS-129
        if (item.getType() == Material.WHEAT ||
                item.getType() == Material.BREAD ||
                item.getType() == Material.PORK ||
                item.getType() == Material.GRILLED_PORK) {
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        String itemType = meta.hasLore() ? meta.getLore().get(0) : "";
        if (!(itemType.equals(ChatColor.GRAY + "Equipment") ||
                itemType.equals(ChatColor.GRAY + "Resource") ||
                itemType.equals(ChatColor.GRAY + "Consumable"))) {
            return false;
        }

        if (meta.getDisplayName().startsWith("Basic")) {
            return false; // basic equipment can't be dropped
        }

        return true;
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (!canBeDropped(event.getItemDrop().getItemStack())) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        event.getDrops().removeIf(item -> !canBeDropped(item));
    }
}
