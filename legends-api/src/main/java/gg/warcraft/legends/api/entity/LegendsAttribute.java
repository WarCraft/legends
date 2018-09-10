package gg.warcraft.legends.api.entity;

import gg.warcraft.monolith.api.entity.attribute.Attribute;

public enum LegendsAttribute implements Attribute {
    STRENGTH("LEGENDS_STRENGTH", "Strength"),
    DEXTERITY("LEGENDS_DEXTERITY", "Dexterity"),
    INTELLIGENCE("LEGENDS_INTELLIGENCE", "Intelligence"),
    VITALITY("LEGENDS_VITALITY", "Vitality"),
    CRIT("LEGENDS_CRIT", "Crit");

    private final String id;
    private final String name;

    LegendsAttribute(String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
