package gg.warcraft.legends.item;

import gg.warcraft.legends.gathering.LegendsGatheringModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class CraftingLoreListener implements Listener {
    private static final ItemStack resourceWheat = LegendsGatheringModule.getWheatDrop();
    private static final ItemStack resourcePorkchop = LegendsGatheringModule.getPorkDrop();

    private ItemStack createConsumableBread() {
        ItemStack item = new ItemStack(Material.BREAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Bread");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Consumable"));
        item.setItemMeta(meta);
        return item;
    }

    private boolean isExpectedResource(ItemStack item, Material expectedType, ItemMeta expectedMeta) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        return item.getType().equals(expectedType) &&
                meta.getDisplayName().equals(expectedMeta.getDisplayName()) &&
                meta.getLore().equals(expectedMeta.getLore());
    }

    public boolean isResourceWheat(ItemStack item) {
        return isExpectedResource(item, Material.WHEAT, resourceWheat.getItemMeta());
    }

    public boolean isResourcePorkchop(ItemStack item) {
        return isExpectedResource(item, Material.PORK, resourcePorkchop.getItemMeta());
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        int otherStuff = 0;
        int numOfResourceWheat = 0;
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (isResourceWheat(item)) {
                numOfResourceWheat += 1;
            } else if (item != null) {
                otherStuff += 1;
            }
        }

        if (numOfResourceWheat == 3 && otherStuff == 0) {
            event.getInventory().setResult(createConsumableBread());
        }
    }

    @EventHandler
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent event) {
        if (isResourcePorkchop(event.getSource())) {
            ItemStack result = event.getResult();
            ItemMeta meta = result.getItemMeta();
            meta.setDisplayName("Cooked Meat");
            meta.setLore(Collections.singletonList(ChatColor.GRAY + "Consumable"));
            result.setItemMeta(meta);

            Furnace furnace = (Furnace) event.getBlock().getState();
            furnace.getInventory().getFuel().setAmount(10);
        }
    }
}
