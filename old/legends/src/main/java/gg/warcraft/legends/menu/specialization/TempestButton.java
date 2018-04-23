package gg.warcraft.legends.menu.specialization;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TempestButton extends Button {
    private final LegendsPlayer player;

    public TempestButton(LegendsPlayer player, Runnable action) {
        super(Material.WOOD_HOE, "Tempest", action);
        this.player = player;

        List<String> lore = new ArrayList<>();
        lore.add("Tempests wield a staff (hoe). Their");
        lore.add("skills offer large area damage.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "[Left click] Fireball");
        lore.add("Fling a ball of fire at your target");
        lore.add("dealing damage and setting them on fire");
        lore.add(ChatColor.YELLOW + "[Right click] Bolt");
        lore.add("Call down lightning at your targeted");
        lore.add("location damaging nearby enemies.");
        lore.add(ChatColor.YELLOW + "[Shift left click] Hail Blast");
        lore.add("Blast an icy torrent in front of you");
        lore.add("damaging and slowing enemies.");
        lore.add(ChatColor.YELLOW + "[Shift right click] Molten Storm");
        lore.add("Conjure a storm of molten rocks at your");
        lore.add("targeted location. Each fireball damages");
        lore.add("and briefly sets enemies on fire.");
        lore.add("");
        lore.add("[Shift Right Click] to change to the");
        lore.add("Tempest specialization.");
        setLoreStrict(lore);
    }
}
