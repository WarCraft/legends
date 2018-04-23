package gg.warcraft.legends.menu.specialization;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class HunterButton extends Button {
    private final LegendsPlayer player;

    public HunterButton(LegendsPlayer player, Runnable action) {
        super(Material.BOW, "Hunter", action);
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("Hunters use bow and arrow. Their skills");
        lore.add("offer damage, control, and mobility.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "[Left click] Toxic Arrow");
        lore.add("Fire a poisoned arrow damaging your target");
        lore.add("and applying poison for a short duration.");
        lore.add(ChatColor.YELLOW + "[Right click] Shoot");
        lore.add("A normal Minecraft bow shot.");
        lore.add(ChatColor.YELLOW + "[Shift left click] Trap");
        lore.add("Place a trap at your feet which roots");
        lore.add("any enemies that trigger it.");
        lore.add(ChatColor.YELLOW + "[Shift right click] Disengage");
        lore.add("Leap backwards a sizable distance.");
        lore.add("");
        lore.add("[Shift Right Click] to change to the");
        lore.add("Hunter specialization.");
        setLoreStrict(lore);
    }
}
