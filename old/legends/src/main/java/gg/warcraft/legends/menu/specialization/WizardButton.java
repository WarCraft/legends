package gg.warcraft.legends.menu.specialization;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class WizardButton extends Button {
    private final LegendsPlayer player;

    public WizardButton(LegendsPlayer player, Runnable action) {
        super(Material.STICK, "Wizard", action);
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("Tempests wield wand (stick) and book. Their");
        lore.add("skills focus on area control, and mobility.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "[Left click] Fireball");
        lore.add("Fling a ball of fire at your target");
        lore.add("dealing damage and setting them on fire");
        lore.add(ChatColor.YELLOW + "[Right click] Arcane Missile");
        lore.add("Cast an arcane missile damaging nearby");
        lore.add("enemies on impact.");
        lore.add(ChatColor.YELLOW + "[Shift left click] Teleport");
        lore.add("Teleport to your targeted location, releasing");
        lore.add("an arcane explosion around you on arrival");
        lore.add("damaging and slowing nearby enemies.");
        lore.add(ChatColor.YELLOW + "[Shift right click] Mass Piggify");
        lore.add("Conjure chaotic energies at your targeted");
        lore.add("location, which after a short delay force");
        lore.add("your enemies to ride uncontrollable pigs");
        lore.add("making them nauseous.");
        lore.add("");
        lore.add("[Shift Right Click] to change to the");
        lore.add("Wizard specialization.");
        setLoreStrict(lore);
    }
}
