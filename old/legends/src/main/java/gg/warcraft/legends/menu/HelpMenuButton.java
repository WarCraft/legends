package gg.warcraft.legends.menu;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class HelpMenuButton extends Button {
    private final LegendsPlayer player;

    public HelpMenuButton(LegendsPlayer player, Runnable action) {
        super(Material.COMPASS, "Help Menu", action);
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add( "[Click] to open the Help Menu.");
        lore.add("");
        lore.add("In the Help Menu you can read");
        lore.add("the Server Manual or contact a");
        lore.add("member of staff.");
        setLoreStrict(lore);
    }
}
