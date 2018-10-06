package gg.warcraft.legends.api.core;

public enum Leaderboard {
    KILLS("leaderboard:kills:"),
    DEATHS("leaderboard:deaths:"),
    DAMAGE_DONE("leaderboard:damagedone:"),
    DAMAGE_TAKEN("leaderboard:damagetaken:"),
    OBJECTIVES_CAPTURED("leaderboard:objectivescapture:"),
    OBJECTIVES_ASSAULTED("leaderboard:objectivesassaulted:"),
    OBJECTIVES_DEFENDED("leaderboard:objectivesdefended:"),
    RANK("leaderboard:favor:"),
    WEALTH("leaderboard:gold:");

    private final String persistenceKey;

    Leaderboard(String persistenceKey) {
        this.persistenceKey = persistenceKey;
    }

    public String getPersistenceKey() {
        return persistenceKey;
    }
}
