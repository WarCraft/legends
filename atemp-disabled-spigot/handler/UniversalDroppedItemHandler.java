package gg.warcraft.legends.spigot.core.handler;

import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;
import java.util.List;

public class UniversalDroppedItemHandler implements Listener {
    private final PlayerCommandService playerCommandService;

    @Inject
    public UniversalDroppedItemHandler(PlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        ItemMeta itemMeta = event.getItemDrop().getItemStack().getItemMeta();
        if (itemMeta == null) {
            return;
        }

        List<String> lore = itemMeta.getLore();
        if (lore == null || lore.isEmpty()) {
            return;
        }

        String itemType = lore.get(0);
        if (!itemType.contains("Equipment")
                && !itemType.contains("Blueprint")
                && !itemType.contains("Consumable")
                && !itemType.contains("Resource")) {
            event.setCancelled(true);
            playerCommandService.sendNotification(event.getPlayer().getUniqueId(), "You can't drop this item.");
        }
    }
}
