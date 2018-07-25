package gg.warcraft.legends.api.item;

import gg.warcraft.monolith.api.util.ColorCode;

public enum ItemRarity {
    COMMON(ColorCode.WHITE),
    RARE(ColorCode.BLUE),
    EPIC(ColorCode.DARK_PURPLE),
    LEGENDARY(ColorCode.GOLD),
    ANCIENT(ColorCode.RED);

    private final ColorCode color;

    ItemRarity(ColorCode color) {
        this.color = color;
    }

    public ColorCode getColor() {
        return color;
    }
}
