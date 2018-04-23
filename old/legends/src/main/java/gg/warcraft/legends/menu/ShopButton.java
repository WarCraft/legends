package gg.warcraft.legends.menu;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class ShopButton extends Button {
    private final LegendsPlayer player;

    public ShopButton(LegendsPlayer player) {
        super(Material.DIAMOND, "WarCraft Shop");
        this.player = player;

        int fragments = (int) player.getDonatorCurrency();
        List<String> lore = new ArrayList<>();
        lore.add("You currently have " + fragments + " fragments.");
        lore.add("");
        lore.add("[CLICK] to browse the shop");
        lore.add("");
        lore.add("[SHIFT RIGHT CLICK] to paste the webshop URL in your chatbox");
        setLoreStrict(lore);
    }

    @Override
    public void click(InventoryClickEvent event) {
        RunnableUtils.runOnNextTick(() -> player.openInventory(new ShopMenu(player)));
    }
}
