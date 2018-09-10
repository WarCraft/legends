package gg.warcraft.legends.api.entity;

import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.ColorHue;

public enum LegendsTeam implements Team {
    ORC("Vorkulth", ColorHue.RED, ColorCode.RED),
    HUMAN("Humenum", ColorHue.AQUA, ColorCode.AQUA),
    DWARF("DWARF_PH", ColorHue.ORANGE, ColorCode.GOLD),
    ELF("ELF_PH", ColorHue.GREEN, ColorCode.GREEN),
    WAYFARER("Wayfarer", ColorHue.WHITE, ColorCode.WHITE);

    private final String name;
    private final ColorHue color;
    private final ColorCode chatColor;

    LegendsTeam(String name, ColorHue color, ColorCode chatColor) {
        this.name = name;
        this.color = color;
        this.chatColor = chatColor;
    }

    @Override
    public String getName() {
        return name;
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
