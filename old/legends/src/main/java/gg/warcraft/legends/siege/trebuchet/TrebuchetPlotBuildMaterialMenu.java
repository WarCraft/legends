package gg.warcraft.legends.siege.trebuchet;

import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TrebuchetPlotBuildMaterialMenu extends Menu {
    public static final ItemStack resourceWood = createWoodItemStack(0);
    public static final ItemStack resourceStone = createStoneItemStack(1);

    public static ItemStack createWoodItemStack(int amount) {
        return createResourceItemStack("Wood", Material.LOG, amount);
    }

    public static ItemStack createStoneItemStack(int amount) {
        return createResourceItemStack("Stone", Material.STONE, amount);
    }

    private static ItemStack createResourceItemStack(String name, Material type, int amount) {
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Resource",
                "",
                ChatColor.GRAY + "Used in the construction of siege equipment."
        ));
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public TrebuchetPlotBuildMaterialMenu(Player viewer, TrebuchetPlot plot) {
        super(viewer, "Trebuchet plot");
        String remWood = "(" + plot.getRemainingWood() + " remaining)";
        String remStone = "(" + plot.getRemainingStone() + " remaining)";
        update(ButtonSlot.FIRST, new Button(Material.WOOD_STEP, "+1 Wood " + remWood, () -> addWoodToPlot(viewer, plot, 1)));
        update(ButtonSlot.SECOND, new Button(Material.LOG, "+10 Wood " + remWood, () -> addWoodToPlot(viewer, plot, 10)));
        update(ButtonSlot.THIRD, new Button(Material.STEP, "+1 Stone " + remStone, () -> addStoneToPlot(viewer, plot, 1)));
        update(ButtonSlot.FOURTH, new Button(Material.STONE, "+10 Stone " + remStone, () -> addStoneToPlot(viewer, plot, 10)));
    }

    private void addWoodToPlot(Player player, TrebuchetPlot plot, int amount) {
        amount = Math.min(plot.getRemainingWood(), amount);
        if (player.getInventory().containsAtLeast(resourceWood, amount)) {
            player.getInventory().removeItem(createWoodItemStack(amount));
            plot.addWood(amount);
        } else {
            player.sendMessage(ChatColor.YELLOW + "Whoops you don't have enough wood!");
        }
    }

    private void addStoneToPlot(Player player, TrebuchetPlot plot, int amount) {
        amount = Math.min(plot.getRemainingStone(), amount);
        if (player.getInventory().containsAtLeast(resourceStone, amount)) {
            player.getInventory().removeItem(createStoneItemStack(amount));
            plot.addStone(amount);
        } else {
            player.sendMessage(ChatColor.YELLOW + "Whoops you don't have enough stone!");
        }
    }
}
