package gg.warcraft.legends.api.entity.player;

public enum LegendsPlayerData {
    TUTORIAL_STEP("tutorialStep"),
    SPECIALIZATION_COOLDOWN("specializationCooldown");

    private final String name;

    LegendsPlayerData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
