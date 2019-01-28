package gg.warcraft.legends.api.entity.player;

public enum LegendsPlayerData {
    TUTORIAL_STEP("tutorialStep"),
    SPECIALIZATION_COOLDOWN("specializationCooldown"),
    RANK_UP_REWARDS_CLAIMED("rankRewardsClaimed");

    private final String name;

    LegendsPlayerData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
