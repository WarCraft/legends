package gg.warcraft.legends.menu.specialization;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ArmsmasterButton extends Button {
    private final LegendsPlayer player;

    public ArmsmasterButton(LegendsPlayer player, Runnable action) {
        super(Material.IRON_SWORD, "Armsmaster", action);
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("Armsmasters wield a sword and a shield.");
        lore.add("Their skills focus on control and defense.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "[Left click] Melee");
        lore.add("A normal Minecraft melee swing.");
        lore.add(ChatColor.YELLOW + "[Right click] Hamstring");
        lore.add("Damage your enemy and slow them for a");
        lore.add("short duration.");
        lore.add(ChatColor.YELLOW + "[Shift left click] Slam");
        lore.add("An overpowering blow that damages the");
        lore.add("target, knocks them back, and makes");
        lore.add("them nauseous for a moderate duration.");
        lore.add(ChatColor.YELLOW + "[Shift right click] Stand Your Ground");
        lore.add("Bunker down to greatly increase all of your");
        lore.add("resistances for a moderate duration.");
        lore.add("");
        lore.add("[SHIFT RIGHT CLICK] to change to the");
        lore.add("Armsmaster specialization.");
        setLoreStrict(lore);
    }
}
