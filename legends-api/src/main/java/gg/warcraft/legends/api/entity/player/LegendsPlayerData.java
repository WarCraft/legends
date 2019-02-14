package gg.warcraft.legends.api.entity.player;

public enum LegendsPlayerData {
    TUTORIAL_STEP("tutorialStep"),
    SPECIALIZATION_COOLDOWN("specializationCooldown"),
    RANK_UP_REWARDS_CLAIMED("rankRewardsClaimed"),
    LAST_DAILY_RESET("lastDailyReset"),
    LAST_WARRY_RESET("lastWarryReset");

    private final String key;

    LegendsPlayerData(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
