package gg.warcraft.legends.api.entity.player;

import gg.warcraft.monolith.api.entity.player.Currency;

public enum LegendsCurrency implements Currency {
    GOLD("Gold"),
    FAVOR("Favor");

    private final String name;

    LegendsCurrency(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
