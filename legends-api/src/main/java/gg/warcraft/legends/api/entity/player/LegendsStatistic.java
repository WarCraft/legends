package gg.warcraft.legends.api.entity.player;

import gg.warcraft.monolith.api.entity.player.Statistic;

public enum LegendsStatistic implements Statistic {
    // Combat Statistics
    DAILY_KILLS("dailyKills"),
    WAR_KILLS("warKills"),
    ALLTIME_KILLS("alltimeKills"),

    DAILY_DEATHS("dailyDeaths"),
    WAR_DEATHS("warDeaths"),
    ALLTIME_DEATHS("alltimeDeaths"),

    DAILY_ASSISTS("dailyAssists"),
    WAR_ASSISTS("warAssists"),
    ALLTIME_ASSISTS("alltimeAssists"),

    DAILY_DAMAGE_DONE("dailyDamageDone"),
    WAR_DAMAGE_DONE("warDamageDone"),
    ALLTIME_DAMAGE_DONE("alltimeDamageDone"),

    DAILY_HEALING_DONE("dailyHealingDone"),
    WAR_HEALING_DONE("warHealingDone"),
    ALLTIME_HEALING_DONE("alltimeHealingDone"),

    DAILY_DAMAGE_TAKEN("dailyDamageTaken"),
    WAR_DAMAGE_TAKEN("warDamageTaken"),
    ALLTIME_DAMAGE_TAKEN("alltimeDamageTaken"),

    // Siege Statistics
    DAILY_CAMPS_CAPTURED("dailyCampsNeutralized"),
    WAR_CAMPS_NEUTRALIZED("warCampsNeutralized"),
    ALLTIME_CAMPS_DEFENDED("alltimeCampsNeutralized"),

    DAILY_OUTPOSTS_CAPTURED("dailyOutpostsCaptured"),
    WAR_OUTPOSTS_NEUTRALIZED("warOutpostsCaptured"),
    ALLTIME_OUTPOSTS_DEFENDED("alltimeOutpostsCaptured"),

    DAILY_KEEPS_CAPTURED("dailyKeepsCaptured"),
    WAR_KEEPS_NEUTRALIZED("warKeepsCaptured"),
    ALLTIME_KEEPS_DEFENDED("alltimeKeepsCaptured"),

    // Misc Statistics
    DAILY_TIME_PLAYED("dailyTimePlayed"),
    WAR_TIME_PLAYED("warTimePlayed"),
    ALLTIME_TIME_PLAYED("alltimeTimePlayed"),

    DAILY_MONSTER_KILLS("dailyMonsterKills"),
    WAR_MONSTER_KILLS("warMonsterKills"),
    ALLTIME_MONSTER_KILLS("alltimeMonsterKills"),

    DAILY_RESOURCES_GATHERED("dailyResourcesGathered"),
    WAR_RESOURCES_GATHERED("warResourcesGathered"),
    ALLTIME_RESOURCES_GATHERED("alltimeResourcesGathered"),

    DAILY_CHESTS_OPENED("dailyChestsOpened"),
    WAR_CHESTS_OPENED("warChestsOpened"),
    ALLTIME_CHESTS_OPENED("alltimeChestsOpened");

    private final String name;

    LegendsStatistic(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
