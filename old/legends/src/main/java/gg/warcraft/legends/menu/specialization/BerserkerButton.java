package gg.warcraft.legends.menu.specialization;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BerserkerButton extends Button {
    private final LegendsPlayer player;

    public BerserkerButton(LegendsPlayer player, Runnable action) {
        super(Material.STONE_AXE, "Berserker", action);
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("Berserkers wield an axe and the skulls");
        lore.add("of their enemies. Their skills focus on");
        lore.add("damage and recovering health.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "[Left click] Melee");
        lore.add("A normal Minecraft melee swing.");
        lore.add(ChatColor.YELLOW + "[Right click] Maul");
        lore.add("Maul your enemy for damage and receive");
        lore.add("absorption for a short duration.");
        lore.add(ChatColor.YELLOW + "[Shift left click] Grudge");
        lore.add("Mark a player with your grudge for a");
        lore.add("moderate duration. Any attacks made");
        lore.add("against them heal you a little bit.");
        lore.add(ChatColor.YELLOW + "[Shift right click] Shatter");
        lore.add("Slam down into the ground in front of");
        lore.add("you, damaging enemies while throwing");
        lore.add("them into the air and making them");
        lore.add("nauseous for a moderate duration.");
        lore.add("");
        lore.add("[SHIFT RIGHT CLICK] to change to the");
        lore.add("Berserker specialization.");
        setLoreStrict(lore);
    }
}
