package gg.warcraft.legends.menu;

import gg.warcraft.legends.RacePicker;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.shop.ShopService;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

// TODO add ButtonBuilder object?
public class RaceButton extends Button {
    private final LegendsPlayer player;

    public RaceButton(LegendsPlayer player) {
        super(Material.BANNER, "Race");
        this.player = player;

        int raceChangeTokens = 0; // TODO add ShopInventory to LegendsPlayer
        ChatColor availabilityColor = raceChangeTokens > 0 ? ChatColor.GREEN : ChatColor.RED;
        List<String> lore = new ArrayList<>();
        String playerRace = ChatColor.YELLOW + player.getRace().getName();
        lore.add("You are currently fighting for the " + playerRace);
        lore.add("");

        // TODO hook up to race change token
        lore.add(availabilityColor + "[CLICK] to change your race");
//        lore.add(availabilityColor + "Changing your race can be done for free on the first day");
//        lore.add(availabilityColor + "of the week or requires a Race Change Token. You have");
//        lore.add(availabilityColor + "" + raceChangeTokens + " tokens. You can buy tokens in the WarCraft shop.");
        setLoreStrict(lore);
    }

    @Override
    public void click(InventoryClickEvent event) {
        RunnableUtils.runOnNextTick(() -> player.openInventory(new RacePicker(player)));
    }
}
