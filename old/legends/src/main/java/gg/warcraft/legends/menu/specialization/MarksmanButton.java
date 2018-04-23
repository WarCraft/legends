package gg.warcraft.legends.menu.specialization;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MarksmanButton extends Button {
    private final LegendsPlayer player;

    public MarksmanButton(LegendsPlayer player, Runnable action) {
        super(Material.BOW, "Marksman", action);
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("Marksmen use bow and arrow. Their skills");
        lore.add("focus on damage while sacrificing mobility.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "[Left click] Power Shot");
        lore.add("Fire a high velocity shot dealing damage");
        lore.add("and knocking back your target.");
        lore.add(ChatColor.YELLOW + "[Right click] Shoot");
        lore.add("A normal Minecraft bow shot.");
        lore.add(ChatColor.YELLOW + "[Shift left click] Focus");
        lore.add("Focus your vision and greatly increase");
        lore.add("your damage while becoming immobile");
        lore.add("(cast again to cancel).");
        lore.add(ChatColor.YELLOW + "[Shift right click] Sneak");
        lore.add("Gain a speed boost and hide in plain");
        lore.add("sight, becoming invisible to enemies.");
        lore.add("");
        lore.add("[Shift Right Click] to change to the");
        lore.add("Marksman specialization.");
        setLoreStrict(lore);
    }
}
