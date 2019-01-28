package gg.warcraft.legends.api.entity.player.rank;

import gg.warcraft.monolith.api.util.ColorCode;

public interface Rank {

    String getName();

    String getFormattedName();

    int getLevel();

    int getPrestige();

    ColorCode getColor();

    int getFavor();

    int getRequiredFavor();
}
