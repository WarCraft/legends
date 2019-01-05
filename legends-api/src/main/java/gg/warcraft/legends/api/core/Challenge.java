package gg.warcraft.legends.api.core;

public enum Challenge {
    // Daily Challenges
    DAILY_KILLS("Daily Kills", 3, 75),

    DAILY_DAMAGE_DONE("Daily Damage Done", 50, 75),
    DAILY_HEALING_DONE("Daily Healing Done", 20, 75),

    DAILY_CAMPS_CAPTURED("Daily Camps Captured", 1, 75),
    DAILY_OUTPOSTS_CAPTURED("Daily Outposts Captured", 1, 75),
    DAILY_KEEPS_CAPTURED("Daily Keeps Captured", 1, 75),

    DAILY_TIME_PLAYED("Daily Time Played", 30, 75),
    DAILY_MONSTER_KILLS("Daily Monster Kills", 10, 75),
    DAILY_RESOURCES_GATHERED("Daily Resources Gathered", 15, 75),
    DAILY_CHESTS_OPENED("Daily Chests Opened", 3, 75),

    // War Challenges
    WAR_KILLS("War Kills", 25, 600),

    WAR_DAMAGE_DONE("War Damage Done", 500, 600),
    WAR_HEALING_DONE("War Healing Done", 200, 600),

    WAR_CAMPS_CAPTURED("War Camps Captured", 5, 600),
    WAR_OUTPOSTS_CAPTURED("War Outposts Captured", 3, 600),
    WAR_KEEPS_CAPTURED("War Keeps Captured", 2, 600),

    WAR_TIME_PLAYED("War Time Played", 300, 600),
    WAR_MONSTER_KILLS("War Monster Kills", 100, 600),
    WAR_RESOURCES_GATHERED("War Resources Gathered", 250, 600),
    WAR_CHESTS_OPENED("War Chests Opened", 35, 600),

    // Alltime Challenges
    ALLTIME_KILLS_1("All-time Kills", 10, 1),
    ALLTIME_KILLS_2("All-time Kills", 100, 10),
    ALLTIME_KILLS_3("All-time Kills", 1000, 100),
    ALLTIME_KILLS_4("All-time Kills", 10000, 1000),
    ALLTIME_KILLS_5("All-time Kills", 100000, 10000),
    ALLTIME_KILLS_6("All-time Kills", 1000000, 100000),
    ALLTIME_DEATHS_1("All-time Deaths", 10, 1),
    ALLTIME_DEATHS_2("All-time Deaths", 100, 10),
    ALLTIME_DEATHS_3("All-time Deaths", 1000, 100),
    ALLTIME_DEATHS_4("All-time Deaths", 10000, 1000),
    ALLTIME_DEATHS_5("All-time Deaths", 100000, 10000),
    ALLTIME_DEATHS_6("All-time Deaths", 1000000, 100000),
    ALLTIME_ASSISTS_1("All-time Assists", 10, 1),
    ALLTIME_ASSISTS_2("All-time Assists", 100, 10),
    ALLTIME_ASSISTS_3("All-time Assists", 1000, 100),
    ALLTIME_ASSISTS_4("All-time Assists", 10000, 1000),
    ALLTIME_ASSISTS_5("All-time Assists", 100000, 10000),
    ALLTIME_ASSISTS_6("All-time Assists", 1000000, 100000),

    ALLTIME_DAMAGE_DONE_1("All-time Damage Done", 10, 1),
    ALLTIME_DAMAGE_DONE_2("All-time Damage Done", 100, 10),
    ALLTIME_DAMAGE_DONE_3("All-time Damage Done", 1000, 100),
    ALLTIME_DAMAGE_DONE_4("All-time Damage Done", 10000, 1000),
    ALLTIME_DAMAGE_DONE_5("All-time Damage Done", 100000, 10000),
    ALLTIME_DAMAGE_DONE_6("All-time Damage Done", 1000000, 100000),
    ALLTIME_HEALING_DONE_1("All-time Healing Done", 10, 1),
    ALLTIME_HEALING_DONE_2("All-time Healing Done", 100, 10),
    ALLTIME_HEALING_DONE_3("All-time Healing Done", 1000, 100),
    ALLTIME_HEALING_DONE_4("All-time Healing Done", 10000, 1000),
    ALLTIME_HEALING_DONE_5("All-time Healing Done", 100000, 10000),
    ALLTIME_HEALING_DONE_6("All-time Healing Done", 1000000, 100000),
    ALLTIME_DAMAGE_TAKEN_1("All-time Damage Taken", 10, 1),
    ALLTIME_DAMAGE_TAKEN_2("All-time Damage Taken", 100, 10),
    ALLTIME_DAMAGE_TAKEN_3("All-time Damage Taken", 1000, 100),
    ALLTIME_DAMAGE_TAKEN_4("All-time Damage Taken", 10000, 1000),
    ALLTIME_DAMAGE_TAKEN_5("All-time Damage Taken", 100000, 10000),
    ALLTIME_DAMAGE_TAKEN_6("All-time Damage Taken", 1000000, 100000),

    ALLTIME_CAMPS_CAPTURED_1("All-time Camps Captured", 10, 1),
    ALLTIME_CAMPS_CAPTURED_2("All-time Camps Captured", 100, 10),
    ALLTIME_CAMPS_CAPTURED_3("All-time Camps Captured", 1000, 100),
    ALLTIME_CAMPS_CAPTURED_4("All-time Camps Captured", 10000, 1000),
    ALLTIME_CAMPS_CAPTURED_5("All-time Camps Captured", 100000, 10000),
    ALLTIME_CAMPS_CAPTURED_6("All-time Camps Captured", 1000000, 100000),
    ALLTIME_CAMPS_NEUTRALIZED_1("All-time Camps Neutralized", 10, 1),
    ALLTIME_CAMPS_NEUTRALIZED_2("All-time Camps Neutralized", 100, 10),
    ALLTIME_CAMPS_NEUTRALIZED_3("All-time Camps Neutralized", 1000, 100),
    ALLTIME_CAMPS_NEUTRALIZED_4("All-time Camps Neutralized", 10000, 1000),
    ALLTIME_CAMPS_NEUTRALIZED_5("All-time Camps Neutralized", 100000, 10000),
    ALLTIME_CAMPS_NEUTRALIZED_6("All-time Camps Neutralized", 1000000, 100000),
    ALLTIME_CAMPS_DEFENDED_1("All-time Camps Defended", 10, 1),
    ALLTIME_CAMPS_DEFENDED_2("All-time Camps Defended", 100, 10),
    ALLTIME_CAMPS_DEFENDED_3("All-time Camps Defended", 1000, 100),
    ALLTIME_CAMPS_DEFENDED_4("All-time Camps Defended", 10000, 1000),
    ALLTIME_CAMPS_DEFENDED_5("All-time Camps Defended", 100000, 10000),
    ALLTIME_CAMPS_DEFENDED_6("All-time Camps Defended", 1000000, 100000),

    ALLTIME_OUTPOSTS_CAPTURED_1("All-time Outposts Captured", 10, 1),
    ALLTIME_OUTPOSTS_CAPTURED_2("All-time Outposts Captured", 100, 10),
    ALLTIME_OUTPOSTS_CAPTURED_3("All-time Outposts Captured", 1000, 100),
    ALLTIME_OUTPOSTS_CAPTURED_4("All-time Outposts Captured", 10000, 1000),
    ALLTIME_OUTPOSTS_CAPTURED_5("All-time Outposts Captured", 100000, 10000),
    ALLTIME_OUTPOSTS_CAPTURED_6("All-time Outposts Captured", 1000000, 100000),
    ALLTIME_OUTPOSTS_NEUTRALIZED_1("All-time Outposts Neutralized", 10, 1),
    ALLTIME_OUTPOSTS_NEUTRALIZED_2("All-time Outposts Neutralized", 100, 10),
    ALLTIME_OUTPOSTS_NEUTRALIZED_3("All-time Outposts Neutralized", 1000, 100),
    ALLTIME_OUTPOSTS_NEUTRALIZED_4("All-time Outposts Neutralized", 10000, 1000),
    ALLTIME_OUTPOSTS_NEUTRALIZED_5("All-time Outposts Neutralized", 100000, 10000),
    ALLTIME_OUTPOSTS_NEUTRALIZED_6("All-time Outposts Neutralized", 1000000, 100000),
    ALLTIME_OUTPOSTS_DEFENDED_1("All-time Outposts Defended", 10, 1),
    ALLTIME_OUTPOSTS_DEFENDED_2("All-time Outposts Defended", 100, 10),
    ALLTIME_OUTPOSTS_DEFENDED_3("All-time Outposts Defended", 1000, 100),
    ALLTIME_OUTPOSTS_DEFENDED_4("All-time Outposts Defended", 10000, 1000),
    ALLTIME_OUTPOSTS_DEFENDED_5("All-time Outposts Defended", 100000, 10000),
    ALLTIME_OUTPOSTS_DEFENDED_6("All-time Outposts Defended", 1000000, 100000),

    ALLTIME_KEEPS_CAPTURED_1("All-time Keeps Captured", 10, 1),
    ALLTIME_KEEPS_CAPTURED_2("All-time Keeps Captured", 100, 10),
    ALLTIME_KEEPS_CAPTURED_3("All-time Keeps Captured", 1000, 100),
    ALLTIME_KEEPS_CAPTURED_4("All-time Keeps Captured", 10000, 1000),
    ALLTIME_KEEPS_CAPTURED_5("All-time Keeps Captured", 100000, 10000),
    ALLTIME_KEEPS_CAPTURED_6("All-time Keeps Captured", 1000000, 100000),
    ALLTIME_KEEPS_NEUTRALIZED_1("All-time Keeps Neutralized", 10, 1),
    ALLTIME_KEEPS_NEUTRALIZED_2("All-time Keeps Neutralized", 100, 10),
    ALLTIME_KEEPS_NEUTRALIZED_3("All-time Keeps Neutralized", 1000, 100),
    ALLTIME_KEEPS_NEUTRALIZED_4("All-time Keeps Neutralized", 10000, 1000),
    ALLTIME_KEEPS_NEUTRALIZED_5("All-time Keeps Neutralized", 100000, 10000),
    ALLTIME_KEEPS_NEUTRALIZED_6("All-time Keeps Neutralized", 1000000, 100000),
    ALLTIME_KEEPS_DEFENDED_1("All-time Keeps Defended", 10, 1),
    ALLTIME_KEEPS_DEFENDED_2("All-time Keeps Defended", 100, 10),
    ALLTIME_KEEPS_DEFENDED_3("All-time Keeps Defended", 1000, 100),
    ALLTIME_KEEPS_DEFENDED_4("All-time Keeps Defended", 10000, 1000),
    ALLTIME_KEEPS_DEFENDED_5("All-time Keeps Defended", 100000, 10000),
    ALLTIME_KEEPS_DEFENDED_6("All-time Keeps Defended", 1000000, 100000),

    ALLTIME_TIME_PLAYED_1("All-time Time Played", 10, 1),
    ALLTIME_TIME_PLAYED_2("All-time Time Played", 100, 10),
    ALLTIME_TIME_PLAYED_3("All-time Time Played", 1000, 100),
    ALLTIME_TIME_PLAYED_4("All-time Time Played", 10000, 1000),
    ALLTIME_TIME_PLAYED_5("All-time Time Played", 100000, 10000),
    ALLTIME_TIME_PLAYED_6("All-time Time Played", 1000000, 100000),
    ALLTIME_MONSTER_KILLS_1("All-time Monster Kills", 10, 1),
    ALLTIME_MONSTER_KILLS_2("All-time Monster Kills", 100, 10),
    ALLTIME_MONSTER_KILLS_3("All-time Monster Kills", 1000, 100),
    ALLTIME_MONSTER_KILLS_4("All-time Monster Kills", 10000, 1000),
    ALLTIME_MONSTER_KILLS_5("All-time Monster Kills", 100000, 10000),
    ALLTIME_MONSTER_KILLS_6("All-time Monster Kills", 1000000, 100000),
    ALLTIME_RESOURCES_GATHERED_1("All-time Resources Gathered", 10, 1),
    ALLTIME_RESOURCES_GATHERED_2("All-time Resources Gathered", 100, 10),
    ALLTIME_RESOURCES_GATHERED_3("All-time Resources Gathered", 1000, 100),
    ALLTIME_RESOURCES_GATHERED_4("All-time Resources Gathered", 10000, 1000),
    ALLTIME_RESOURCES_GATHERED_5("All-time Resources Gathered", 100000, 10000),
    ALLTIME_RESOURCES_GATHERED_6("All-time Resources Gathered", 1000000, 100000),
    ALLTIME_CHESTS_OPENED_1("All-time Chests Opened", 10, 1),
    ALLTIME_CHESTS_OPENED_2("All-time Chests Opened", 100, 10),
    ALLTIME_CHESTS_OPENED_3("All-time Chests Opened", 1000, 100),
    ALLTIME_CHESTS_OPENED_4("All-time Chests Opened", 10000, 1000),
    ALLTIME_CHESTS_OPENED_5("All-time Chests Opened", 100000, 10000),
    ALLTIME_CHESTS_OPENED_6("All-time Chests Opened", 1000000, 100000),

    // Special Challenges
    SPECIAL_KILLS("1337", 1337, 5000),

    SPECIAL_CAMPS_CAPTURED("Why?", 666, 10000),
    SPECIAL_OUTPOSTS_CAPTURED("Blaze um!", 420, 15000),
    SPECIAL_KEEPS_CAPTURED("Sexy keep you got there", 69, 5000),

    SPECIAL_RESOURCES_GATHERED("Nice", 80085, 20000);

    private final String name;
    private final int requiredValue;
    private final int favorReward;

    Challenge(String name, int requiredValue, int favorReward) {
        this.name = name;
        this.requiredValue = requiredValue;
        this.favorReward = favorReward;
    }

    public String getName() {
        return name;
    }

    public int getRequiredValue() {
        return requiredValue;
    }

    public int getFavorReward() {
        return favorReward;
    }
}
