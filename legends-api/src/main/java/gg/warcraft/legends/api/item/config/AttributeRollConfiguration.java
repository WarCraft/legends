package gg.warcraft.legends.api.item.config;

import gg.warcraft.legends.api.entity.LegendsAttribute;

public interface AttributeRollConfiguration {

    LegendsAttribute getAttribute();

    int getLowRoll();

    int getHighRoll();
}
