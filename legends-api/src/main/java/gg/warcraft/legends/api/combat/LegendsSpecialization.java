package gg.warcraft.legends.api.combat;

public enum LegendsSpecialization {
    ARMSMASTER("Armsmaster"),
    BERSERKER("Berserker"),
    HUNTER("Hunter"),
    MARKSMAN("Marksman"),
    TEMPEST("Tempest"),
    WIZARD("Wizard");

    private final String name;

    LegendsSpecialization(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
