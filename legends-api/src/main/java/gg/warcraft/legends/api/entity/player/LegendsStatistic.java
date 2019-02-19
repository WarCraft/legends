package gg.warcraft.legends.api.entity.player;

import gg.warcraft.monolith.api.entity.player.Statistic;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum LegendsStatistic implements Statistic {
    // Combat Statistics
    DAILY_KILLS("Daily Kills", "dailyKills"),
    WARRY_KILLS("War Kills", "warryKills"),
    ALLTIME_KILLS("All-time Kills", "alltimeKills"),

    DAILY_DEATHS("Daily Deaths", "dailyDeaths"),
    WARRY_DEATHS("War Deaths", "warryDeaths"),
    ALLTIME_DEATHS("All-time Deaths", "alltimeDeaths"),

    DAILY_ASSISTS("Daily Assists", "dailyAssists"),
    WARRY_ASSISTS("War Assists", "warryAssists"),
    ALLTIME_ASSISTS("All-time assists", "alltimeAssists"),

    DAILY_DAMAGE_DONE("Daily Damage Done", "dailyDamageDone"),
    WARRY_DAMAGE_DONE("War Damage Done", "warryDamageDone"),
    ALLTIME_DAMAGE_DONE("All-time Damage Done", "alltimeDamageDone"),

    DAILY_HEALING_DONE("Daily Healing Done", "dailyHealingDone"),
    WARRY_HEALING_DONE("War Healing Done", "warryHealingDone"),
    ALLTIME_HEALING_DONE("All-time Healing Done", "alltimeHealingDone"),

    DAILY_DAMAGE_TAKEN("Daily Damage Taken", "dailyDamageTaken"),
    WARRY_DAMAGE_TAKEN("War Damage Taken", "warryDamageTaken"),
    ALLTIME_DAMAGE_TAKEN("All-time Damage Taken", "alltimeDamageTaken"),

    DAILY_HEALING_TAKEN("Daily Healing Taken", "dailyHealingTaken"),
    WARRY_HEALING_TAKEN("War Healing Taken", "warryHealingTaken"),
    ALLTIME_HEALING_TAKEN("All-time Healing Taken", "alltimeHealingTaken"),

    // Siege Statistics
    DAILY_CAMPS_CAPTURED("Daily Camps Captured", "dailyCampsCaptured"),
    DAILY_CAMPS_NEUTRALIZED("Daily Camps Neutralized", "dailyCampsNeutralized"),
    DAILY_CAMPS_DEFENDED("Daily Camps Defended", "dailyCampsDefended"),
    WARRY_CAMPS_CAPTURED("War Camps Captured", "warryCampsCaptured"),
    WARRY_CAMPS_NEUTRALIZED("War Camps Neutralized", "warryCampsNeutralized"),
    WARRY_CAMPS_DEFENDED("War Camps Defended", "warryCampsDefended"),
    ALLTIME_CAMPS_CAPTURED("All-time Camps Captured", "alltimeCampsCaptured"),
    ALLTIME_CAMPS_NEUTRALIZED("All-time Camps Neutralized", "alltimeCampsNeutralized"),
    ALLTIME_CAMPS_DEFENDED("All-time Camps Defended", "alltimeCampsDefended"),

    DAILY_OUTPOSTS_CAPTURED("Daily Outposts Captured", "dailyOutpostsCaptured"),
    DAILY_OUTPOSTS_NEUTRALIZED("Daily Outposts Neutralized", "dailyOutpostsNeutralized"),
    DAILY_OUTPOSTS_DEFENDED("Daily Outposts Defended", "dailyOutpostsDefended"),
    WARRY_OUTPOSTS_CAPTURED("War Outposts Captured", "warryOutpostsCaptured"),
    WARRY_OUTPOSTS_NEUTRALIZED("War Outposts Neutralized", "warryOutpostsNeutralized"),
    WARRY_OUTPOSTS_DEFENDED("War Outposts Defended", "warryOutpostsDefended"),
    ALLTIME_OUTPOSTS_CAPTURED("All-time Outposts Captured", "alltimeOutpostsCaptured"),
    ALLTIME_OUTPOSTS_NEUTRALIZED("All-time Outposts Neutralized", "alltimeOutpostsNeutralized"),
    ALLTIME_OUTPOSTS_DEFENDED("All-time Outposts Defended", "alltimeOutpostsDefended"),

    DAILY_KEEPS_CAPTURED("Daily Keeps Captured", "dailyKeepsCaptured"),
    DAILY_KEEPS_NEUTRALIZED("Daily Keeps Neutralized", "dailyKeepsNeutralized"),
    DAILY_KEEPS_DEFENDED("Daily Keeps Defended", "dailyKeepsDefended"),
    WARRY_KEEPS_CAPTURED("War Keeps Captured", "warryKeepsCaptured"),
    WARRY_KEEPS_NEUTRALIZED("War Keeps Neutralized", "warryKeepsNeutralized"),
    WARRY_KEEPS_DEFENDED("War Keeps Defended", "warryKeepsDefended"),
    ALLTIME_KEEPS_CAPTURED("All-time Keeps Captured", "alltimeKeepsCaptured"),
    ALLTIME_KEEPS_NEUTRALIZED("All-time Keeps Neutralized", "alltimeKeepsNeutralized"),
    ALLTIME_KEEPS_DEFENDED("All-time Keeps Defended", "alltimeKeepsDefended"),

    // Misc Statistics
    DAILY_TIME_PLAYED("Daily Time Played", "dailyTimePlayed"),
    WARRY_TIME_PLAYED("War Time Played", "warryTimePlayed"),
    ALLTIME_TIME_PLAYED("All-time Time Played", "alltimeTimePlayed"),

    DAILY_MONSTER_KILLS("Daily Monster Kills", "dailyMonsterKills"),
    WARRY_MONSTER_KILLS("War Monster Kills", "warryMonsterKills"),
    ALLTIME_MONSTER_KILLS("All-time Monster Kills", "alltimeMonsterKills"),

    DAILY_RESOURCES_GATHERED("Daily Resources Gathered", "dailyResourcesGathered"),
    WARRY_RESOURCES_GATHERED("War Resources Gathered", "warryResourcesGathered"),
    ALLTIME_RESOURCES_GATHERED("All-time Resources Gathered", "alltimeResourcesGathered"),

    DAILY_CRATES_OPENED("Daily Crates Opened", "dailyCratesOpened"),
    WARRY_CRATES_OPENED("War Crates Opened", "warryCratesOpened"),
    ALLTIME_CRATES_OPENED("All-time Crates Opened", "alltimeCratesOpened");

    private static final EnumSet<LegendsStatistic> dailies;
    private static final EnumSet<LegendsStatistic> warries;
    private static final EnumSet<LegendsStatistic> allTimes;

    static {
        Supplier<EnumSet<LegendsStatistic>> setSupplier =
                () -> EnumSet.noneOf(LegendsStatistic.class);

        dailies = Arrays.stream(LegendsStatistic.values())
                .filter(LegendsStatistic::isDaily)
                .collect(Collectors.toCollection(setSupplier));

        warries = Arrays.stream(LegendsStatistic.values())
                .filter(LegendsStatistic::isWarry)
                .collect(Collectors.toCollection(setSupplier));

        allTimes = Arrays.stream(LegendsStatistic.values())
                .filter(LegendsStatistic::isAllTime)
                .collect(Collectors.toCollection(setSupplier));
    }

    public static EnumSet<LegendsStatistic> getDailies() {
        return EnumSet.copyOf(dailies);
    }

    public static EnumSet<LegendsStatistic> getWarries() {
        return EnumSet.copyOf(warries);
    }

    public static EnumSet<LegendsStatistic> getAllTimes() {
        return EnumSet.copyOf(allTimes);
    }

    private final String name;
    private final String key;

    LegendsStatistic(String name, String key) {
        this.name = name;
        this.key = key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKey() {
        return key;
    }

    public boolean isDaily() {
        return name().contains("DAILY");
    }

    public boolean isWarry() {
        return name().contains("WARRY");
    }

    public boolean isAllTime() {
        return name().contains("ALLTIME");
    }
}
