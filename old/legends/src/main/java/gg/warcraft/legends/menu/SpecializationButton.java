package gg.warcraft.legends.menu;

import gg.warcraft.legends.menu.specialization.SpecializationMenu;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class SpecializationButton extends Button {
    private final LegendsPlayer player;

    public SpecializationButton(LegendsPlayer player) {
        super(player.getSpecialization().getIcon(), "Specialization");
        this.player = player;

        List<String> lore = new ArrayList<>();
        String playerSpec = player.getSpecialization().name().toLowerCase();
        playerSpec = playerSpec.substring(0, 1).toUpperCase() + playerSpec.substring(1);
        String prefix = StringUtils.isVowel(playerSpec.charAt(0)) ? "an " : "a ";
        lore.add("You are currently fighting as " + prefix + ChatColor.YELLOW + playerSpec);
        lore.add("");
        lore.add("[CLICK] to change your specialization");
        setLoreStrict(lore);
    }

    @Override
    public void click(InventoryClickEvent event) {
        RunnableUtils.runOnNextTick(() -> player.openInventory(new SpecializationMenu(player)));
    }
}
