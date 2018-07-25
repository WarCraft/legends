package gg.warcraft.legends.api.entity.player.rank;

import gg.warcraft.monolith.api.util.ColorCode;

public enum RankPrefix {
    WOOD("Wood", ColorCode.DARK_GREEN),
    STONE("Stone", ColorCode.GRAY),
    IRON("Iron", ColorCode.DARK_GRAY),
    GOLD("Gold", ColorCode.GOLD),
    EMERALD("Emerald", ColorCode.GREEN),
    DIAMOND("Diamond", ColorCode.AQUA),
    OBSIDIAN("Obsidian", ColorCode.DARK_PURPLE);

    private final String name;
    private final ColorCode color;

    RankPrefix(String name, ColorCode color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ColorCode getColor() {
        return color;
    }
}
