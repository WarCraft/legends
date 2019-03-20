package gg.warcraft.legends.api.core.challenge;

import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.entity.player.Statistic;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum Challenge {
    // Daily Challenges
    DAILY_KILLS("Daily Kills", LegendsPlayerData.DAILY_KILLS_CHALLENGE, LegendsStatistic.DAILY_KILLS, 3, 75),

    DAILY_DAMAGE_DONE("Daily Damage Done", LegendsPlayerData.DAILY_DAMAGE_DONE_CHALLENGE, LegendsStatistic.DAILY_DAMAGE_DONE, 50, 75),
    DAILY_HEALING_DONE("Daily Healing Done", LegendsPlayerData.DAILY_HEALING_DONE_CHALLENGE, LegendsStatistic.DAILY_HEALING_DONE, 20, 75),

    DAILY_CAMPS_CAPTURED("Daily Camps Captured", LegendsPlayerData.DAILY_CAMPS_CAPTURED_CHALLENGE, LegendsStatistic.DAILY_CAMPS_CAPTURED, 1, 75),
    DAILY_OUTPOSTS_CAPTURED("Daily Outposts Captured", LegendsPlayerData.DAILY_OUTPOSTS_CAPTURED_CHALLENGE, LegendsStatistic.DAILY_OUTPOSTS_CAPTURED, 1, 75),
    DAILY_KEEPS_CAPTURED("Daily Keeps Captured", LegendsPlayerData.DAILY_KEEPS_CAPTURED_CHALLENGE, LegendsStatistic.DAILY_KEEPS_CAPTURED, 1, 75),

    DAILY_TIME_PLAYED("Daily Time Played", LegendsPlayerData.DAILY_TIME_PLAYED_CHALLENGE, LegendsStatistic.DAILY_TIME_PLAYED, 30, 75),
    DAILY_MONSTER_KILLS("Daily Monster Kills", LegendsPlayerData.DAILY_MONSTER_KILLS_CHALLENGE, LegendsStatistic.DAILY_MONSTER_KILLS, 10, 75),
    DAILY_RESOURCES_GATHERED("Daily Resources Gathered", LegendsPlayerData.DAILY_RESOURCES_GATHERED_CHALLENGE, LegendsStatistic.DAILY_RESOURCES_GATHERED, 15, 75),
    DAILY_CRATES_OPENED("Daily Crates Opened", LegendsPlayerData.DAILY_CRATES_OPENED_CHALLENGE, LegendsStatistic.DAILY_CRATES_OPENED, 3, 75),

    // War Challenges
    WARRY_KILLS("War Kills", LegendsPlayerData.WARRY_KILLS_CHALLENGE, LegendsStatistic.WARRY_KILLS, 25, 600),

    WARRY_DAMAGE_DONE("War Damage Done", LegendsPlayerData.WARRY_DAMAGE_DONE_CHALLENGE, LegendsStatistic.WARRY_DAMAGE_DONE, 500, 600),
    WARRY_HEALING_DONE("War Healing Done", LegendsPlayerData.WARRY_HEALING_DONE_CHALLENGE, LegendsStatistic.WARRY_HEALING_DONE, 200, 600),

    WARRY_CAMPS_CAPTURED("War Camps Captured", LegendsPlayerData.WARRY_CAMPS_CAPTURED_CHALLENGE, LegendsStatistic.WARRY_CAMPS_CAPTURED, 5, 600),
    WARRY_OUTPOSTS_CAPTURED("War Outposts Captured", LegendsPlayerData.WARRY_OUTPOSTS_CAPTURED_CHALLENGE, LegendsStatistic.WARRY_OUTPOSTS_CAPTURED, 3, 600),
    WARRY_KEEPS_CAPTURED("War Keeps Captured", LegendsPlayerData.WARRY_KEEPS_CAPTURED_CHALLENGE, LegendsStatistic.WARRY_KEEPS_CAPTURED, 2, 600),

    WARRY_TIME_PLAYED("War Time Played", LegendsPlayerData.WARRY_TIME_PLAYED_CHALLENGE, LegendsStatistic.WARRY_TIME_PLAYED, 300, 600),
    WARRY_MONSTER_KILLS("War Monster Kills", LegendsPlayerData.WARRY_MONSTER_KILLS_CHALLENGE, LegendsStatistic.WARRY_MONSTER_KILLS, 100, 600),
    WARRY_RESOURCES_GATHERED("War Resources Gathered", LegendsPlayerData.WARRY_RESOURCES_GATHERED_CHALLENGE, LegendsStatistic.WARRY_RESOURCES_GATHERED, 250, 600),
    WARRY_CRATES_OPENED("War Crates Opened", LegendsPlayerData.WARRY_CRATES_OPENED_CHALLENGE, LegendsStatistic.WARRY_CRATES_OPENED, 35, 600),

    // Alltime Challenges
    ALLTIME_KILLS_1("All-time Kills", LegendsPlayerData.ALLTIME_KILLS_1_CHALLENGE, LegendsStatistic.ALLTIME_KILLS, 10, 1),
    ALLTIME_KILLS_2("All-time Kills", LegendsPlayerData.ALLTIME_KILLS_2_CHALLENGE, LegendsStatistic.ALLTIME_KILLS, 100, 10),
    ALLTIME_KILLS_3("All-time Kills", LegendsPlayerData.ALLTIME_KILLS_3_CHALLENGE, LegendsStatistic.ALLTIME_KILLS, 1000, 100),
    ALLTIME_KILLS_4("All-time Kills", LegendsPlayerData.ALLTIME_KILLS_4_CHALLENGE, LegendsStatistic.ALLTIME_KILLS, 10000, 1000),
    ALLTIME_KILLS_5("All-time Kills", LegendsPlayerData.ALLTIME_KILLS_5_CHALLENGE, LegendsStatistic.ALLTIME_KILLS, 100000, 10000),
    ALLTIME_KILLS_6("All-time Kills", LegendsPlayerData.ALLTIME_KILLS_6_CHALLENGE, LegendsStatistic.ALLTIME_KILLS, 1000000, 100000),
    ALLTIME_DEATHS_1("All-time Deaths", LegendsPlayerData.ALLTIME_DEATHS_1_CHALLENGE, LegendsStatistic.ALLTIME_DEATHS, 10, 1),
    ALLTIME_DEATHS_2("All-time Deaths", LegendsPlayerData.ALLTIME_DEATHS_2_CHALLENGE, LegendsStatistic.ALLTIME_DEATHS, 100, 10),
    ALLTIME_DEATHS_3("All-time Deaths", LegendsPlayerData.ALLTIME_DEATHS_3_CHALLENGE, LegendsStatistic.ALLTIME_DEATHS, 1000, 100),
    ALLTIME_DEATHS_4("All-time Deaths", LegendsPlayerData.ALLTIME_DEATHS_4_CHALLENGE, LegendsStatistic.ALLTIME_DEATHS, 10000, 1000),
    ALLTIME_DEATHS_5("All-time Deaths", LegendsPlayerData.ALLTIME_DEATHS_5_CHALLENGE, LegendsStatistic.ALLTIME_DEATHS, 100000, 10000),
    ALLTIME_DEATHS_6("All-time Deaths", LegendsPlayerData.ALLTIME_DEATHS_6_CHALLENGE, LegendsStatistic.ALLTIME_DEATHS, 1000000, 100000),
    ALLTIME_ASSISTS_1("All-time Assists", LegendsPlayerData.ALLTIME_ASSISTS_1_CHALLENGE, LegendsStatistic.ALLTIME_ASSISTS, 10, 1),
    ALLTIME_ASSISTS_2("All-time Assists", LegendsPlayerData.ALLTIME_ASSISTS_2_CHALLENGE, LegendsStatistic.ALLTIME_ASSISTS, 100, 10),
    ALLTIME_ASSISTS_3("All-time Assists", LegendsPlayerData.ALLTIME_ASSISTS_3_CHALLENGE, LegendsStatistic.ALLTIME_ASSISTS, 1000, 100),
    ALLTIME_ASSISTS_4("All-time Assists", LegendsPlayerData.ALLTIME_ASSISTS_4_CHALLENGE, LegendsStatistic.ALLTIME_ASSISTS, 10000, 1000),
    ALLTIME_ASSISTS_5("All-time Assists", LegendsPlayerData.ALLTIME_ASSISTS_5_CHALLENGE, LegendsStatistic.ALLTIME_ASSISTS, 100000, 10000),
    ALLTIME_ASSISTS_6("All-time Assists", LegendsPlayerData.ALLTIME_ASSISTS_6_CHALLENGE, LegendsStatistic.ALLTIME_ASSISTS, 1000000, 100000),

    ALLTIME_DAMAGE_DONE_1("All-time Damage Done", LegendsPlayerData.ALLTIME_DAMAGE_DONE_1_CHALLENGE, LegendsStatistic.ALLTIME_DAMAGE_DONE, 10, 1),
    ALLTIME_DAMAGE_DONE_2("All-time Damage Done", LegendsPlayerData.ALLTIME_DAMAGE_DONE_2_CHALLENGE, LegendsStatistic.ALLTIME_DAMAGE_DONE, 100, 10),
    ALLTIME_DAMAGE_DONE_3("All-time Damage Done", LegendsPlayerData.ALLTIME_DAMAGE_DONE_3_CHALLENGE, LegendsStatistic.ALLTIME_DAMAGE_DONE, 1000, 100),
    ALLTIME_DAMAGE_DONE_4("All-time Damage Done", LegendsPlayerData.ALLTIME_DAMAGE_DONE_4_CHALLENGE, LegendsStatistic.ALLTIME_DAMAGE_DONE, 10000, 1000),
    ALLTIME_DAMAGE_DONE_5("All-time Damage Done", LegendsPlayerData.ALLTIME_DAMAGE_DONE_5_CHALLENGE, LegendsStatistic.ALLTIME_DAMAGE_DONE, 100000, 10000),
    ALLTIME_DAMAGE_DONE_6("All-time Damage Done", LegendsPlayerData.ALLTIME_DAMAGE_DONE_6_CHALLENGE, LegendsStatistic.ALLTIME_DAMAGE_DONE, 1000000, 100000),
    ALLTIME_HEALING_DONE_1("All-time Healing Done", LegendsPlayerData.ALLTIME_HEALING_DONE_1_CHALLENGE, LegendsStatistic.ALLTIME_HEALING_DONE, 10, 1),
    ALLTIME_HEALING_DONE_2("All-time Healing Done", LegendsPlayerData.ALLTIME_HEALING_DONE_2_CHALLENGE, LegendsStatistic.ALLTIME_HEALING_DONE, 100, 10),
    ALLTIME_HEALING_DONE_3("All-time Healing Done", LegendsPlayerData.ALLTIME_HEALING_DONE_3_CHALLENGE, LegendsStatistic.ALLTIME_HEALING_DONE, 1000, 100),
    ALLTIME_HEALING_DONE_4("All-time Healing Done", LegendsPlayerData.ALLTIME_HEALING_DONE_4_CHALLENGE, LegendsStatistic.ALLTIME_HEALING_DONE, 10000, 1000),
    ALLTIME_HEALING_DONE_5("All-time Healing Done", LegendsPlayerData.ALLTIME_HEALING_DONE_5_CHALLENGE, LegendsStatistic.ALLTIME_HEALING_DONE, 100000, 10000),
    ALLTIME_HEALING_DONE_6("All-time Healing Done", LegendsPlayerData.ALLTIME_HEALING_DONE_6_CHALLENGE, LegendsStatistic.ALLTIME_HEALING_DONE, 1000000, 100000),

    ALLTIME_CAMPS_CAPTURED_1("All-time Camps Captured", LegendsPlayerData.ALLTIME_CAMPS_CAPTURED_1_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_CAPTURED, 10, 1),
    ALLTIME_CAMPS_CAPTURED_2("All-time Camps Captured", LegendsPlayerData.ALLTIME_CAMPS_CAPTURED_2_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_CAPTURED, 100, 10),
    ALLTIME_CAMPS_CAPTURED_3("All-time Camps Captured", LegendsPlayerData.ALLTIME_CAMPS_CAPTURED_3_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_CAPTURED, 1000, 100),
    ALLTIME_CAMPS_CAPTURED_4("All-time Camps Captured", LegendsPlayerData.ALLTIME_CAMPS_CAPTURED_4_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_CAPTURED, 10000, 1000),
    ALLTIME_CAMPS_CAPTURED_5("All-time Camps Captured", LegendsPlayerData.ALLTIME_CAMPS_CAPTURED_5_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_CAPTURED, 100000, 10000),
    ALLTIME_CAMPS_CAPTURED_6("All-time Camps Captured", LegendsPlayerData.ALLTIME_CAMPS_CAPTURED_6_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_CAPTURED, 1000000, 100000),
    ALLTIME_CAMPS_NEUTRALIZED_1("All-time Camps Neutralized", LegendsPlayerData.ALLTIME_CAMPS_NEUTRALIZED_1_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_NEUTRALIZED, 10, 1),
    ALLTIME_CAMPS_NEUTRALIZED_2("All-time Camps Neutralized", LegendsPlayerData.ALLTIME_CAMPS_NEUTRALIZED_2_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_NEUTRALIZED, 100, 10),
    ALLTIME_CAMPS_NEUTRALIZED_3("All-time Camps Neutralized", LegendsPlayerData.ALLTIME_CAMPS_NEUTRALIZED_3_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_NEUTRALIZED, 1000, 100),
    ALLTIME_CAMPS_NEUTRALIZED_4("All-time Camps Neutralized", LegendsPlayerData.ALLTIME_CAMPS_NEUTRALIZED_4_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_NEUTRALIZED, 10000, 1000),
    ALLTIME_CAMPS_NEUTRALIZED_5("All-time Camps Neutralized", LegendsPlayerData.ALLTIME_CAMPS_NEUTRALIZED_5_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_NEUTRALIZED, 100000, 10000),
    ALLTIME_CAMPS_NEUTRALIZED_6("All-time Camps Neutralized", LegendsPlayerData.ALLTIME_CAMPS_NEUTRALIZED_6_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_NEUTRALIZED, 1000000, 100000),
    ALLTIME_CAMPS_DEFENDED_1("All-time Camps Defended", LegendsPlayerData.ALLTIME_CAMPS_DEFENDED_1_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_DEFENDED, 10, 1),
    ALLTIME_CAMPS_DEFENDED_2("All-time Camps Defended", LegendsPlayerData.ALLTIME_CAMPS_DEFENDED_2_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_DEFENDED, 100, 10),
    ALLTIME_CAMPS_DEFENDED_3("All-time Camps Defended", LegendsPlayerData.ALLTIME_CAMPS_DEFENDED_3_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_DEFENDED, 1000, 100),
    ALLTIME_CAMPS_DEFENDED_4("All-time Camps Defended", LegendsPlayerData.ALLTIME_CAMPS_DEFENDED_4_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_DEFENDED, 10000, 1000),
    ALLTIME_CAMPS_DEFENDED_5("All-time Camps Defended", LegendsPlayerData.ALLTIME_CAMPS_DEFENDED_5_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_DEFENDED, 100000, 10000),
    ALLTIME_CAMPS_DEFENDED_6("All-time Camps Defended", LegendsPlayerData.ALLTIME_CAMPS_DEFENDED_6_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_DEFENDED, 1000000, 100000),

    ALLTIME_OUTPOSTS_CAPTURED_1("All-time Outposts Captured", LegendsPlayerData.ALLTIME_OUTPOSTS_CAPTURED_1_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, 10, 1),
    ALLTIME_OUTPOSTS_CAPTURED_2("All-time Outposts Captured", LegendsPlayerData.ALLTIME_OUTPOSTS_CAPTURED_2_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, 100, 10),
    ALLTIME_OUTPOSTS_CAPTURED_3("All-time Outposts Captured", LegendsPlayerData.ALLTIME_OUTPOSTS_CAPTURED_3_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, 1000, 100),
    ALLTIME_OUTPOSTS_CAPTURED_4("All-time Outposts Captured", LegendsPlayerData.ALLTIME_OUTPOSTS_CAPTURED_4_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, 10000, 1000),
    ALLTIME_OUTPOSTS_CAPTURED_5("All-time Outposts Captured", LegendsPlayerData.ALLTIME_OUTPOSTS_CAPTURED_5_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, 100000, 10000),
    ALLTIME_OUTPOSTS_CAPTURED_6("All-time Outposts Captured", LegendsPlayerData.ALLTIME_OUTPOSTS_CAPTURED_6_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, 1000000, 100000),
    ALLTIME_OUTPOSTS_NEUTRALIZED_1("All-time Outposts Neutralized", LegendsPlayerData.ALLTIME_OUTPOSTS_NEUTRALIZED_1_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_NEUTRALIZED, 10, 1),
    ALLTIME_OUTPOSTS_NEUTRALIZED_2("All-time Outposts Neutralized", LegendsPlayerData.ALLTIME_OUTPOSTS_NEUTRALIZED_2_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_NEUTRALIZED, 100, 10),
    ALLTIME_OUTPOSTS_NEUTRALIZED_3("All-time Outposts Neutralized", LegendsPlayerData.ALLTIME_OUTPOSTS_NEUTRALIZED_3_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_NEUTRALIZED, 1000, 100),
    ALLTIME_OUTPOSTS_NEUTRALIZED_4("All-time Outposts Neutralized", LegendsPlayerData.ALLTIME_OUTPOSTS_NEUTRALIZED_4_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_NEUTRALIZED, 10000, 1000),
    ALLTIME_OUTPOSTS_NEUTRALIZED_5("All-time Outposts Neutralized", LegendsPlayerData.ALLTIME_OUTPOSTS_NEUTRALIZED_5_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_NEUTRALIZED, 100000, 10000),
    ALLTIME_OUTPOSTS_NEUTRALIZED_6("All-time Outposts Neutralized", LegendsPlayerData.ALLTIME_OUTPOSTS_NEUTRALIZED_6_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_NEUTRALIZED, 1000000, 100000),
    ALLTIME_OUTPOSTS_DEFENDED_1("All-time Outposts Defended", LegendsPlayerData.ALLTIME_OUTPOSTS_DEFENDED_1_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED, 10, 1),
    ALLTIME_OUTPOSTS_DEFENDED_2("All-time Outposts Defended", LegendsPlayerData.ALLTIME_OUTPOSTS_DEFENDED_2_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED, 100, 10),
    ALLTIME_OUTPOSTS_DEFENDED_3("All-time Outposts Defended", LegendsPlayerData.ALLTIME_OUTPOSTS_DEFENDED_3_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED, 1000, 100),
    ALLTIME_OUTPOSTS_DEFENDED_4("All-time Outposts Defended", LegendsPlayerData.ALLTIME_OUTPOSTS_DEFENDED_4_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED, 10000, 1000),
    ALLTIME_OUTPOSTS_DEFENDED_5("All-time Outposts Defended", LegendsPlayerData.ALLTIME_OUTPOSTS_DEFENDED_5_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED, 100000, 10000),
    ALLTIME_OUTPOSTS_DEFENDED_6("All-time Outposts Defended", LegendsPlayerData.ALLTIME_OUTPOSTS_DEFENDED_6_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED, 1000000, 100000),

    ALLTIME_KEEPS_CAPTURED_1("All-time Keeps Captured", LegendsPlayerData.ALLTIME_KEEPS_CAPTURED_1_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_CAPTURED, 10, 1),
    ALLTIME_KEEPS_CAPTURED_2("All-time Keeps Captured", LegendsPlayerData.ALLTIME_KEEPS_CAPTURED_2_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_CAPTURED, 100, 10),
    ALLTIME_KEEPS_CAPTURED_3("All-time Keeps Captured", LegendsPlayerData.ALLTIME_KEEPS_CAPTURED_3_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_CAPTURED, 1000, 100),
    ALLTIME_KEEPS_CAPTURED_4("All-time Keeps Captured", LegendsPlayerData.ALLTIME_KEEPS_CAPTURED_4_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_CAPTURED, 10000, 1000),
    ALLTIME_KEEPS_CAPTURED_5("All-time Keeps Captured", LegendsPlayerData.ALLTIME_KEEPS_CAPTURED_5_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_CAPTURED, 100000, 10000),
    ALLTIME_KEEPS_CAPTURED_6("All-time Keeps Captured", LegendsPlayerData.ALLTIME_KEEPS_CAPTURED_6_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_CAPTURED, 1000000, 100000),
    ALLTIME_KEEPS_NEUTRALIZED_1("All-time Keeps Neutralized", LegendsPlayerData.ALLTIME_KEEPS_NEUTRALIZED_1_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_NEUTRALIZED, 10, 1),
    ALLTIME_KEEPS_NEUTRALIZED_2("All-time Keeps Neutralized", LegendsPlayerData.ALLTIME_KEEPS_NEUTRALIZED_2_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_NEUTRALIZED, 100, 10),
    ALLTIME_KEEPS_NEUTRALIZED_3("All-time Keeps Neutralized", LegendsPlayerData.ALLTIME_KEEPS_NEUTRALIZED_3_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_NEUTRALIZED, 1000, 100),
    ALLTIME_KEEPS_NEUTRALIZED_4("All-time Keeps Neutralized", LegendsPlayerData.ALLTIME_KEEPS_NEUTRALIZED_4_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_NEUTRALIZED, 10000, 1000),
    ALLTIME_KEEPS_NEUTRALIZED_5("All-time Keeps Neutralized", LegendsPlayerData.ALLTIME_KEEPS_NEUTRALIZED_5_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_NEUTRALIZED, 100000, 10000),
    ALLTIME_KEEPS_NEUTRALIZED_6("All-time Keeps Neutralized", LegendsPlayerData.ALLTIME_KEEPS_NEUTRALIZED_6_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_NEUTRALIZED, 1000000, 100000),
    ALLTIME_KEEPS_DEFENDED_1("All-time Keeps Defended", LegendsPlayerData.ALLTIME_KEEPS_DEFENDED_1_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_DEFENDED, 10, 1),
    ALLTIME_KEEPS_DEFENDED_2("All-time Keeps Defended", LegendsPlayerData.ALLTIME_KEEPS_DEFENDED_2_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_DEFENDED, 100, 10),
    ALLTIME_KEEPS_DEFENDED_3("All-time Keeps Defended", LegendsPlayerData.ALLTIME_KEEPS_DEFENDED_3_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_DEFENDED, 1000, 100),
    ALLTIME_KEEPS_DEFENDED_4("All-time Keeps Defended", LegendsPlayerData.ALLTIME_KEEPS_DEFENDED_4_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_DEFENDED, 10000, 1000),
    ALLTIME_KEEPS_DEFENDED_5("All-time Keeps Defended", LegendsPlayerData.ALLTIME_KEEPS_DEFENDED_5_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_DEFENDED, 100000, 10000),
    ALLTIME_KEEPS_DEFENDED_6("All-time Keeps Defended", LegendsPlayerData.ALLTIME_KEEPS_DEFENDED_6_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_DEFENDED, 1000000, 100000),

    ALLTIME_TIME_PLAYED_1("All-time Time Played", LegendsPlayerData.ALLTIME_TIME_PLAYED_1_CHALLENGE, LegendsStatistic.ALLTIME_TIME_PLAYED, 10, 1),
    ALLTIME_TIME_PLAYED_2("All-time Time Played", LegendsPlayerData.ALLTIME_TIME_PLAYED_2_CHALLENGE, LegendsStatistic.ALLTIME_TIME_PLAYED, 100, 10),
    ALLTIME_TIME_PLAYED_3("All-time Time Played", LegendsPlayerData.ALLTIME_TIME_PLAYED_3_CHALLENGE, LegendsStatistic.ALLTIME_TIME_PLAYED, 1000, 100),
    ALLTIME_TIME_PLAYED_4("All-time Time Played", LegendsPlayerData.ALLTIME_TIME_PLAYED_4_CHALLENGE, LegendsStatistic.ALLTIME_TIME_PLAYED, 10000, 1000),
    ALLTIME_TIME_PLAYED_5("All-time Time Played", LegendsPlayerData.ALLTIME_TIME_PLAYED_5_CHALLENGE, LegendsStatistic.ALLTIME_TIME_PLAYED, 100000, 10000),
    ALLTIME_TIME_PLAYED_6("All-time Time Played", LegendsPlayerData.ALLTIME_TIME_PLAYED_6_CHALLENGE, LegendsStatistic.ALLTIME_TIME_PLAYED, 1000000, 100000),
    ALLTIME_MONSTER_KILLS_1("All-time Monster Kills", LegendsPlayerData.ALLTIME_MONSTER_KILLS_1_CHALLENGE, LegendsStatistic.ALLTIME_MONSTER_KILLS, 10, 1),
    ALLTIME_MONSTER_KILLS_2("All-time Monster Kills", LegendsPlayerData.ALLTIME_MONSTER_KILLS_2_CHALLENGE, LegendsStatistic.ALLTIME_MONSTER_KILLS, 100, 10),
    ALLTIME_MONSTER_KILLS_3("All-time Monster Kills", LegendsPlayerData.ALLTIME_MONSTER_KILLS_3_CHALLENGE, LegendsStatistic.ALLTIME_MONSTER_KILLS, 1000, 100),
    ALLTIME_MONSTER_KILLS_4("All-time Monster Kills", LegendsPlayerData.ALLTIME_MONSTER_KILLS_4_CHALLENGE, LegendsStatistic.ALLTIME_MONSTER_KILLS, 10000, 1000),
    ALLTIME_MONSTER_KILLS_5("All-time Monster Kills", LegendsPlayerData.ALLTIME_MONSTER_KILLS_5_CHALLENGE, LegendsStatistic.ALLTIME_MONSTER_KILLS, 100000, 10000),
    ALLTIME_MONSTER_KILLS_6("All-time Monster Kills", LegendsPlayerData.ALLTIME_MONSTER_KILLS_6_CHALLENGE, LegendsStatistic.ALLTIME_MONSTER_KILLS, 1000000, 100000),
    ALLTIME_RESOURCES_GATHERED_1("All-time Resources Gathered", LegendsPlayerData.ALLTIME_RESOURCES_GATHERED_1_CHALLENGE, LegendsStatistic.ALLTIME_RESOURCES_GATHERED, 10, 1),
    ALLTIME_RESOURCES_GATHERED_2("All-time Resources Gathered", LegendsPlayerData.ALLTIME_RESOURCES_GATHERED_2_CHALLENGE, LegendsStatistic.ALLTIME_RESOURCES_GATHERED, 100, 10),
    ALLTIME_RESOURCES_GATHERED_3("All-time Resources Gathered", LegendsPlayerData.ALLTIME_RESOURCES_GATHERED_3_CHALLENGE, LegendsStatistic.ALLTIME_RESOURCES_GATHERED, 1000, 100),
    ALLTIME_RESOURCES_GATHERED_4("All-time Resources Gathered", LegendsPlayerData.ALLTIME_RESOURCES_GATHERED_4_CHALLENGE, LegendsStatistic.ALLTIME_RESOURCES_GATHERED, 10000, 1000),
    ALLTIME_RESOURCES_GATHERED_5("All-time Resources Gathered", LegendsPlayerData.ALLTIME_RESOURCES_GATHERED_5_CHALLENGE, LegendsStatistic.ALLTIME_RESOURCES_GATHERED, 100000, 10000),
    ALLTIME_RESOURCES_GATHERED_6("All-time Resources Gathered", LegendsPlayerData.ALLTIME_RESOURCES_GATHERED_6_CHALLENGE, LegendsStatistic.ALLTIME_RESOURCES_GATHERED, 1000000, 100000),
    ALLTIME_CRATES_OPENED_1("All-time Crates Opened", LegendsPlayerData.ALLTIME_CRATES_OPENED_1_CHALLENGE, LegendsStatistic.ALLTIME_CRATES_OPENED, 10, 1),
    ALLTIME_CRATES_OPENED_2("All-time Crates Opened", LegendsPlayerData.ALLTIME_CRATES_OPENED_2_CHALLENGE, LegendsStatistic.ALLTIME_CRATES_OPENED, 100, 10),
    ALLTIME_CRATES_OPENED_3("All-time Crates Opened", LegendsPlayerData.ALLTIME_CRATES_OPENED_3_CHALLENGE, LegendsStatistic.ALLTIME_CRATES_OPENED, 1000, 100),
    ALLTIME_CRATES_OPENED_4("All-time Crates Opened", LegendsPlayerData.ALLTIME_CRATES_OPENED_4_CHALLENGE, LegendsStatistic.ALLTIME_CRATES_OPENED, 10000, 1000),
    ALLTIME_CRATES_OPENED_5("All-time Crates Opened", LegendsPlayerData.ALLTIME_CRATES_OPENED_5_CHALLENGE, LegendsStatistic.ALLTIME_CRATES_OPENED, 100000, 10000),
    ALLTIME_CRATES_OPENED_6("All-time Crates Opened", LegendsPlayerData.ALLTIME_CRATES_OPENED_6_CHALLENGE, LegendsStatistic.ALLTIME_CRATES_OPENED, 1000000, 100000),

    // Special Challenges
    SPECIAL_KILLS("1337", LegendsPlayerData.SPECIAL_KILLS_CHALLENGE, LegendsStatistic.ALLTIME_KILLS, 1337, 5000),

    SPECIAL_CAMPS_CAPTURED("Why? For the glory of satan of course!", LegendsPlayerData.SPECIAL_CAMPS_CAPTURED_CHALLENGE, LegendsStatistic.ALLTIME_CAMPS_CAPTURED, 666, 10000),
    SPECIAL_OUTPOSTS_CAPTURED("Blaze um!", LegendsPlayerData.SPECIAL_OUTPOSTS_CAPTURED_CHALLENGE, LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, 420, 15000),
    SPECIAL_KEEPS_CAPTURED("Sexy keep you got there", LegendsPlayerData.SPECIAL_KEEPS_CAPTURED_CHALLENGE, LegendsStatistic.ALLTIME_KEEPS_CAPTURED, 69, 5000),

    SPECIAL_RESOURCES_GATHERED("Nice", LegendsPlayerData.SPECIAL_RESOURCES_GATHERED_CHALLENGE, LegendsStatistic.ALLTIME_RESOURCES_GATHERED, 80085, 20000);

    private static final EnumSet<Challenge> dailies;
    private static final EnumSet<Challenge> warries;
    private static final EnumSet<Challenge> allTimes;
    private static final EnumSet<Challenge> specials;

    static {
        Supplier<EnumSet<Challenge>> setSupplier =
                () -> EnumSet.noneOf(Challenge.class);

        dailies = Arrays.stream(Challenge.values())
                .filter(Challenge::isDaily)
                .collect(Collectors.toCollection(setSupplier));

        warries = Arrays.stream(Challenge.values())
                .filter(Challenge::isWarry)
                .collect(Collectors.toCollection(setSupplier));

        allTimes = Arrays.stream(Challenge.values())
                .filter(Challenge::isAllTime)
                .collect(Collectors.toCollection(setSupplier));

        specials = Arrays.stream(Challenge.values())
                .filter(Challenge::isSpecial)
                .collect(Collectors.toCollection(setSupplier));
    }

    public static EnumSet<Challenge> getDailies() {
        return EnumSet.copyOf(dailies);
    }

    public static EnumSet<Challenge> getWarries() {
        return EnumSet.copyOf(warries);
    }

    public static EnumSet<Challenge> getAllTimes() {
        return EnumSet.copyOf(allTimes);
    }

    public static EnumSet<Challenge> getSpecials() {
        return EnumSet.copyOf(specials);
    }

    private final String name;
    private final LegendsPlayerData playerData;
    private final Statistic statistic;
    private final int requiredValue;
    private final int favorReward;

    Challenge(String name, LegendsPlayerData playerData, Statistic statistic,
              int requiredStatisticValue, int favorReward) {
        this.name = name;
        this.playerData = playerData;
        this.statistic = statistic;
        this.requiredValue = requiredStatisticValue;
        this.favorReward = favorReward;
    }

    public String getName() {
        return name;
    }

    public LegendsPlayerData getPlayerData() {
        return playerData;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public int getRequiredStatisticValue() {
        return requiredValue;
    }

    public int getFavorReward() {
        return favorReward;
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

    public boolean isSpecial() {
        return name().contains("SPECIAL");
    }
}
