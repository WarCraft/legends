package gg.warcraft.legends.api.entity;

import gg.warcraft.monolith.api.entity.attribute.Attribute;

public enum LegendsAttribute implements Attribute {
    STRENGTH("Strength"),
    DEXTERITY("Dexterity"),
    INTELLIGENCE("Intelligence"),
    CRIT("Crit"),
    VITALITY("Vitality");

    private final String name;

    LegendsAttribute(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
