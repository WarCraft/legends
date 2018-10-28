package gg.warcraft.legends.api.entity.player.rank;

import gg.warcraft.monolith.api.util.ColorCode;

public interface Rank {

    String getName();

    int getLevel();

    int getPrestige();

    ColorCode getColor();
}
