package gg.warcraft.legends.api.combat;

public enum LegendsSpecialization {
    ARMSMASTER("Armsmaster", LegendsSpecializationType.WARRIOR),
    BERSERKER("Berserker", LegendsSpecializationType.WARRIOR),
    HUNTER("Hunter", LegendsSpecializationType.RANGER),
    MARKSMAN("Marksman", LegendsSpecializationType.RANGER),
    TEMPEST("Tempest", LegendsSpecializationType.MAGE),
    WIZARD("Wizard", LegendsSpecializationType.MAGE);

    private final String name;
    private final LegendsSpecializationType type;

    LegendsSpecialization(String name, LegendsSpecializationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public LegendsSpecializationType getType() {
        return type;
    }
}
