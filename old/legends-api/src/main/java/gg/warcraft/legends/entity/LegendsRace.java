package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.Race;
import org.bukkit.ChatColor;

public enum LegendsRace implements Race {
    DWARF("Argûl", ChatColor.GOLD),
    ELF("Ivrae-dalin", ChatColor.GREEN),
    GOBLIN("GOBLIN_NAME_PLACEHOLDER", ChatColor.DARK_RED),
    HUMAN("Humenum", ChatColor.AQUA),
    ORC("Vorkulth", ChatColor.RED),
    UNDEAD("UNDEAD_NAME_PLACEHOLDER", ChatColor.GRAY);

    private final String name;
    private final ChatColor color;

    LegendsRace(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ChatColor getColor() {
        return color;
    }
}
