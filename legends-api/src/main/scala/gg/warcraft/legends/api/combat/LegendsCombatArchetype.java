/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat;

public enum LegendsCombatArchetype {
    WARRIOR("Warrior"),
    HUNTER("Hunter"),
    MAGE("Mage");

    private final String string;

    LegendsCombatArchetype(String string) {
        this.string = string;
    }


    @Override
    public String toString() {
        return string;
    }
}
