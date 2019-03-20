package gg.warcraft.legends.api.core.leaderboard;

public enum Leaderboard {
    // Combat Leaderboards
    DAILY_KILLS("Daily Kills", "leaderboard:daily:kills:"),
    WAR_KILLS("War Kills", "leaderboard:war:kills:"),
    ALLTIME_KILLS("All-time Kills", "leaderboard:alltime:kills:"),

    DAILY_DAMAGE_DONE("Daily DamageDone", "leaderboard:daily:damagedone:"),
    WAR_DAMAGE_DONE("War Damage Done", "leaderboard:war:damagedone:"),
    ALLTIME_DAMAGE_DONE("All-time Damage Done", "leaderboard:alltime:damagedone:"),

    DAILY_HEALING_DONE("Daily HealingDone", "leaderboard:daily:healingdone:"),
    WAR_HEALING_DONE("War Healing Done", "leaderboard:war:healingdone:"),
    ALLTIME_HEALING_DONE("All-time Healing Done", "leaderboard:alltime:healingdone:"),

    // Siege Leaderboards
    DAILY_OBJECTIVES_CAPTURED("Daily Objectives Captured", "leaderboard:daily:objectivescapture:"),
    WAR_OBJECTIVES_CAPTURED("War Objectives Captured", "leaderboard:war:objectivescaptured:"),
    ALLTIME_OBJECTIVES_CAPTURED("All-time Objectives Captured", "leaderboard:alltime:objectivescaptured:"),
    DAILY_OBJECTIVES_NEUTRALIZED("Daily Objectives Neutralized", "leaderboard:daily:objectivesneutralized:"),
    WAR_OBJECTIVES_NEUTRALIZED("War Objectives Neutralized", "leaderboard:war:objectivesneutralized:"),
    ALLTIME_OBJECTIVES_NEUTRALIZED("All-time Objectives Neutralized", "leaderboard:alltime:objectivesneutralized:"),
    DAILY_OBJECTIVES_DEFENDED("Daily Objectives Defended", "leaderboard:daily:objectivesdefended:"),
    WAR_OBJECTIVES_DEFENDED("War Objectives Defended", "leaderboard:war:objectivesdefended:"),
    ALLTIME_OBJECTIVES_DEFENDED("All-time Objectives Defended", "leaderboard:alltime:objectivesdefended:"),

    // Currency Leaderboards
    DAILY_FAVOR_GAINED("Daily Favor Gained", "leaderboard:daily:favorgained"),
    WAR_FAVOR_GAINED("War Favor Gained", "leaderboard:war:favorgained"),
    ALLTIME_FAVOR_GAINED("All-time Favor Gained", "leaderboard:alltime:favorgained"),

    DAILY_GOLD_GAINED("Daily Gold Gained", "leaderboard:daily:goldgained"),
    WAR_GOLD_GAINED("War Gold Gained", "leaderboard:war:goldgained"),
    ALLTIME_GOLD_GAINED("All-time Gold Gained", "leaderboard:alltime:goldgained"),

    // Misc Leaderboards
    DAILY_TIME_PLAYED("Daily Time Played", "leaderboard:daily:timeplayed"),
    WAR_TIME_PLAYED("War Time Played", "leaderboard:war:timeplayed"),
    ALLTIME_TIME_PLAYED("All-time Time Played", "leaderboard:alltime:timeplayed"),

    DAILY_MONSTER_KILLS("Daily Monster Kills", "leaderboard:daily:monsterkills"),
    WARRY_MONSTER_KILLS("War Monster Kills", "leaderboard:war:monsterkills"),
    ALLTIME_MONSTER_KILLS("All-time Monster Kills", "leaderboard:alltime:monsterkills"),

    DAILY_RESOURCES_GATHERED("Daily Resources Gathered", "leaderboard:daily:resourcesgathered"),
    WARRY_RESOURCES_GATHERED("War Resources Gathered", "leaderboard:war:resourcesgathered"),
    ALLTIME_RESOURCES_GATHERED("All-time Resources Gathered", "leaderboard:alltime:resourcesgathered"),

    DAILY_CRATES_OPENED("Daily Crates Opened", "leaderboard:daily:cratesopened"),
    WARRY_CRATES_OPENED("War Crates Opened", "leaderboard:war:cratesopened"),
    ALLTIME_CRATES_OPENED("All-time Crates Opened", "leaderboard:alltime:cratesopened");

    private final String name;
    private final String key;

    Leaderboard(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
