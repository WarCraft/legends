package gg.warcraft.legends.app.entity.player.rank;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.util.ColorCode;

public class SimpleRank implements Rank {
    private final String name;
    private final int level;
    private final ColorCode color;

    public SimpleRank(String name, int level, ColorCode color) {
        this.name = name;
        this.level = level;
        this.color = color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public ColorCode getColor() {
        return color;
    }
}

/*
String prestigePrefix = new String(new char[prestige]).replace("\0", prestigeChar);
        return prestigePrefix + " " + name;
 */