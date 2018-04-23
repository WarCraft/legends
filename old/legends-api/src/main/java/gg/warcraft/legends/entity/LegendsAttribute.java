package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.Attribute;

public enum LegendsAttribute implements Attribute {
    // Primary
    STRENGTH(0, "Strength", "legends.strength"),
    DEXTERITY(0, "Dexterity", "legends.dexterity"),
    INTELLIGENCE(0, "Intelligence", "legends.intelligence"),

    // Secondary
    VITALITY(0, "Vitality", "legends.vitality"),
    CRIT(0, "Crit", "legends.crit");

    private final double defaultValue;
    private final String name;
    private final String nbtName;

    LegendsAttribute(double defaultValue, String name, String nbtName) {
        this.defaultValue = defaultValue;
        this.name = name;
        this.nbtName = nbtName;
    }

    @Override
    public double getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNbtName() {
        return nbtName;
    }
}
