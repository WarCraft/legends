package gg.warcraft.legends.api.entity;

import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.ColorHue;

public enum LegendsTeam implements Team {
    ORC("Orc", "Orcs", ColorHue.RED, ColorCode.RED),
    HUMAN("Human", "Humans", ColorHue.AQUA, ColorCode.AQUA),
    DWARF("Dwarf", "Dwarves", ColorHue.ORANGE, ColorCode.GOLD),
    ELF("Elf", "Elves", ColorHue.GREEN, ColorCode.GREEN),
    WAYFARER("Wayfarer", "Wayfarers", ColorHue.WHITE, ColorCode.WHITE);

    private final String name;
    private final String plural;
    private final ColorHue color;
    private final ColorCode chatColor;

    LegendsTeam(String name, String plural, ColorHue color, ColorCode chatColor) {
        this.name = name;
        this.plural = plural;
        this.color = color;
        this.chatColor = chatColor;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPlural() {
        return plural;
    }

    @Override
    public ColorHue getColor() {
        return color;
    }

    @Override
    public ColorCode getChatColor() {
        return chatColor;
    }
}
