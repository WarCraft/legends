package gg.warcraft.legends.app.entity.player.rank;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.Objects;

public class SimpleRank implements Rank {
    private final String name;
    private final int level;
    private final int prestige;
    private final ColorCode color;

    public SimpleRank(String name, int level, int prestige, ColorCode color) {
        this.name = name;
        this.level = level;
        this.prestige = prestige;
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
    public int getPrestige() {
        return prestige;
    }

    @Override
    public ColorCode getColor() {
        return color;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SimpleRank other = (SimpleRank) object;
        return Objects.equals(getName(), other.getName())
                && Objects.equals(getPrestige(), other.getPrestige())
                && Objects.equals(getColor(), other.getColor());
    }
}
