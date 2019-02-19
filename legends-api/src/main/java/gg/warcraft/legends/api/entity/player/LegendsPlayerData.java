package gg.warcraft.legends.api.entity.player;

public enum LegendsPlayerData {
    TUTORIAL_STEP("tutorialStep"),
    SPECIALIZATION_COOLDOWN("specializationCooldown"),
    RANK_UP_REWARDS_CLAIMED("rankRewardsClaimed"),
    LAST_DAILY_RESET("lastDailyReset"),
    LAST_WARRY_RESET("lastWarryReset"),

    // Daily Challenges
    DAILY_KILLS_CHALLENGE("challengeDailyKills"),

    DAILY_DAMAGE_DONE_CHALLENGE("challengeDailyDamageDone"),
    DAILY_HEALING_DONE_CHALLENGE("challengeDailyHealingDone"),

    DAILY_CAMPS_CAPTURED_CHALLENGE("challengeDailyCampsCaptured"),
    DAILY_OUTPOSTS_CAPTURED_CHALLENGE("challengeDailyOutpostsCaptured"),
    DAILY_KEEPS_CAPTURED_CHALLENGE("challengeDailyKeepsCaptured"),

    DAILY_TIME_PLAYED_CHALLENGE("challengeDailyTimePlayed"),
    DAILY_MONSTER_KILLS_CHALLENGE("challengeDailyMonsterKills"),
    DAILY_RESOURCES_GATHERED_CHALLENGE("challengeDailyResourcesGathered"),
    DAILY_CRATES_OPENED_CHALLENGE("challengeDailyCratesOpened"),

    //War Challenges
    WARRY_KILLS_CHALLENGE("challengeWarKills"),

    WARRY_DAMAGE_DONE_CHALLENGE("challengeWarDamageDone"),
    WARRY_HEALING_DONE_CHALLENGE("challengeWarHealingDone"),

    WARRY_CAMPS_CAPTURED_CHALLENGE("challengeWarCampsCaptured"),
    WARRY_OUTPOSTS_CAPTURED_CHALLENGE("challengeWarOutpostsCaptured"),
    WARRY_KEEPS_CAPTURED_CHALLENGE("challengeWarKeepsCaptured"),

    WARRY_TIME_PLAYED_CHALLENGE("challengeWarTimePlayed"),
    WARRY_MONSTER_KILLS_CHALLENGE("challengeWarMonsterKills"),
    WARRY_RESOURCES_GATHERED_CHALLENGE("challengeWarResourcesGathered"),
    WARRY_CRATES_OPENED_CHALLENGE("challengeWarCratesOpened"),

    //All-time Challenges
    ALLTIME_KILLS_1_CHALLENGE("challengeAlltimeKills"),
    ALLTIME_KILLS_2_CHALLENGE("challengeAlltimeKills"),
    ALLTIME_KILLS_3_CHALLENGE("challengeAlltimeKills"),
    ALLTIME_KILLS_4_CHALLENGE("challengeAlltimeKills"),
    ALLTIME_KILLS_5_CHALLENGE("challengeAlltimeKills"),
    ALLTIME_KILLS_6_CHALLENGE("challengeAlltimeKills"),
    ALLTIME_DEATHS_1_CHALLENGE("challengeAlltimeDeaths"),
    ALLTIME_DEATHS_2_CHALLENGE("challengeAlltimeDeaths"),
    ALLTIME_DEATHS_3_CHALLENGE("challengeAlltimeDeaths"),
    ALLTIME_DEATHS_4_CHALLENGE("challengeAlltimeDeaths"),
    ALLTIME_DEATHS_5_CHALLENGE("challengeAlltimeDeaths"),
    ALLTIME_DEATHS_6_CHALLENGE("challengeAlltimeDeaths"),
    ALLTIME_ASSISTS_1_CHALLENGE("challengeAlltimeAssists"),
    ALLTIME_ASSISTS_2_CHALLENGE("challengeAlltimeAssists"),
    ALLTIME_ASSISTS_3_CHALLENGE("challengeAlltimeAssists"),
    ALLTIME_ASSISTS_4_CHALLENGE("challengeAlltimeAssists"),
    ALLTIME_ASSISTS_5_CHALLENGE("challengeAlltimeAssists"),
    ALLTIME_ASSISTS_6_CHALLENGE("challengeAlltimeAssists"),

    ALLTIME_DAMAGE_DONE_1_CHALLENGE("challengeAlltimeDamageDone"),
    ALLTIME_DAMAGE_DONE_2_CHALLENGE("challengeAlltimeDamageDone"),
    ALLTIME_DAMAGE_DONE_3_CHALLENGE("challengeAlltimeDamageDone"),
    ALLTIME_DAMAGE_DONE_4_CHALLENGE("challengeAlltimeDamageDone"),
    ALLTIME_DAMAGE_DONE_5_CHALLENGE("challengeAlltimeDamageDone"),
    ALLTIME_DAMAGE_DONE_6_CHALLENGE("challengeAlltimeDamageDone"),
    ALLTIME_HEALING_DONE_1_CHALLENGE("challengeAlltimeHealingDone"),
    ALLTIME_HEALING_DONE_2_CHALLENGE("challengeAlltimeHealingDone"),
    ALLTIME_HEALING_DONE_3_CHALLENGE("challengeAlltimeHealingDone"),
    ALLTIME_HEALING_DONE_4_CHALLENGE("challengeAlltimeHealingDone"),
    ALLTIME_HEALING_DONE_5_CHALLENGE("challengeAlltimeHealingDone"),
    ALLTIME_HEALING_DONE_6_CHALLENGE("challengeAlltimeHealingDone"),
    ALLTIME_DAMAGE_TAKEN_1_CHALLENGE("challengeAlltimeDamageTaken"),
    ALLTIME_DAMAGE_TAKEN_2_CHALLENGE("challengeAlltimeDamageTaken"),
    ALLTIME_DAMAGE_TAKEN_3_CHALLENGE("challengeAlltimeDamageTaken"),
    ALLTIME_DAMAGE_TAKEN_4_CHALLENGE("challengeAlltimeDamageTaken"),
    ALLTIME_DAMAGE_TAKEN_5_CHALLENGE("challengeAlltimeDamageTaken"),
    ALLTIME_DAMAGE_TAKEN_6_CHALLENGE("challengeAlltimeDamageTaken"),

    ALLTIME_CAMPS_CAPTURED_1_CHALLENGE("challengeAlltimeCampsCaptured"),
    ALLTIME_CAMPS_CAPTURED_2_CHALLENGE("challengeAlltimeCampsCaptured"),
    ALLTIME_CAMPS_CAPTURED_3_CHALLENGE("challengeAlltimeCampsCaptured"),
    ALLTIME_CAMPS_CAPTURED_4_CHALLENGE("challengeAlltimeCampsCaptured"),
    ALLTIME_CAMPS_CAPTURED_5_CHALLENGE("challengeAlltimeCampsCaptured"),
    ALLTIME_CAMPS_CAPTURED_6_CHALLENGE("challengeAlltimeCampsCaptured"),
    ALLTIME_CAMPS_NEUTRALIZED_1_CHALLENGE("challengeAlltimeCampsNeutralized"),
    ALLTIME_CAMPS_NEUTRALIZED_2_CHALLENGE("challengeAlltimeCampsNeutralized"),
    ALLTIME_CAMPS_NEUTRALIZED_3_CHALLENGE("challengeAlltimeCampsNeutralized"),
    ALLTIME_CAMPS_NEUTRALIZED_4_CHALLENGE("challengeAlltimeCampsNeutralized"),
    ALLTIME_CAMPS_NEUTRALIZED_5_CHALLENGE("challengeAlltimeCampsNeutralized"),
    ALLTIME_CAMPS_NEUTRALIZED_6_CHALLENGE("challengeAlltimeCampsNeutralized"),
    ALLTIME_CAMPS_DEFENDED_1_CHALLENGE("challengeAlltimeCampsDefended"),
    ALLTIME_CAMPS_DEFENDED_2_CHALLENGE("challengeAlltimeCampsDefended"),
    ALLTIME_CAMPS_DEFENDED_3_CHALLENGE("challengeAlltimeCampsDefended"),
    ALLTIME_CAMPS_DEFENDED_4_CHALLENGE("challengeAlltimeCampsDefended"),
    ALLTIME_CAMPS_DEFENDED_5_CHALLENGE("challengeAlltimeCampsDefended"),
    ALLTIME_CAMPS_DEFENDED_6_CHALLENGE("challengeAlltimeCampsDefended"),

    ALLTIME_OUTPOSTS_CAPTURED_1_CHALLENGE("challengeAlltimeOutpostsCaptured"),
    ALLTIME_OUTPOSTS_CAPTURED_2_CHALLENGE("challengeAlltimeOutpostsCaptured"),
    ALLTIME_OUTPOSTS_CAPTURED_3_CHALLENGE("challengeAlltimeOutpostsCaptured"),
    ALLTIME_OUTPOSTS_CAPTURED_4_CHALLENGE("challengeAlltimeOutpostsCaptured"),
    ALLTIME_OUTPOSTS_CAPTURED_5_CHALLENGE("challengeAlltimeOutpostsCaptured"),
    ALLTIME_OUTPOSTS_CAPTURED_6_CHALLENGE("challengeAlltimeOutpostsCaptured"),
    ALLTIME_OUTPOSTS_NEUTRALIZED_1_CHALLENGE("challengeAlltimeOutpostsNeutralized"),
    ALLTIME_OUTPOSTS_NEUTRALIZED_2_CHALLENGE("challengeAlltimeOutpostsNeutralized"),
    ALLTIME_OUTPOSTS_NEUTRALIZED_3_CHALLENGE("challengeAlltimeOutpostsNeutralized"),
    ALLTIME_OUTPOSTS_NEUTRALIZED_4_CHALLENGE("challengeAlltimeOutpostsNeutralized"),
    ALLTIME_OUTPOSTS_NEUTRALIZED_5_CHALLENGE("challengeAlltimeOutpostsNeutralized"),
    ALLTIME_OUTPOSTS_NEUTRALIZED_6_CHALLENGE("challengeAlltimeOutpostsNeutralized"),
    ALLTIME_OUTPOSTS_DEFENDED_1_CHALLENGE("challengeAlltimeOutpostsDefended"),
    ALLTIME_OUTPOSTS_DEFENDED_2_CHALLENGE("challengeAlltimeOutpostsDefended"),
    ALLTIME_OUTPOSTS_DEFENDED_3_CHALLENGE("challengeAlltimeOutpostsDefended"),
    ALLTIME_OUTPOSTS_DEFENDED_4_CHALLENGE("challengeAlltimeOutpostsDefended"),
    ALLTIME_OUTPOSTS_DEFENDED_5_CHALLENGE("challengeAlltimeOutpostsDefended"),
    ALLTIME_OUTPOSTS_DEFENDED_6_CHALLENGE("challengeAlltimeOutpostsDefended"),

    ALLTIME_KEEPS_CAPTURED_1_CHALLENGE("challengeAlltimeKeepsCaptured"),
    ALLTIME_KEEPS_CAPTURED_2_CHALLENGE("challengeAlltimeKeepsCaptured"),
    ALLTIME_KEEPS_CAPTURED_3_CHALLENGE("challengeAlltimeKeepsCaptured"),
    ALLTIME_KEEPS_CAPTURED_4_CHALLENGE("challengeAlltimeKeepsCaptured"),
    ALLTIME_KEEPS_CAPTURED_5_CHALLENGE("challengeAlltimeKeepsCaptured"),
    ALLTIME_KEEPS_CAPTURED_6_CHALLENGE("challengeAlltimeKeepsCaptured"),
    ALLTIME_KEEPS_NEUTRALIZED_1_CHALLENGE("challengeAlltimeKeepsNeutralized"),
    ALLTIME_KEEPS_NEUTRALIZED_2_CHALLENGE("challengeAlltimeKeepsNeutralized"),
    ALLTIME_KEEPS_NEUTRALIZED_3_CHALLENGE("challengeAlltimeKeepsNeutralized"),
    ALLTIME_KEEPS_NEUTRALIZED_4_CHALLENGE("challengeAlltimeKeepsNeutralized"),
    ALLTIME_KEEPS_NEUTRALIZED_5_CHALLENGE("challengeAlltimeKeepsNeutralized"),
    ALLTIME_KEEPS_NEUTRALIZED_6_CHALLENGE("challengeAlltimeKeepsNeutralized"),
    ALLTIME_KEEPS_DEFENDED_1_CHALLENGE("challengeAlltimeKeepsDefended"),
    ALLTIME_KEEPS_DEFENDED_2_CHALLENGE("challengeAlltimeKeepsDefended"),
    ALLTIME_KEEPS_DEFENDED_3_CHALLENGE("challengeAlltimeKeepsDefended"),
    ALLTIME_KEEPS_DEFENDED_4_CHALLENGE("challengeAlltimeKeepsDefended"),
    ALLTIME_KEEPS_DEFENDED_5_CHALLENGE("challengeAlltimeKeepsDefended"),
    ALLTIME_KEEPS_DEFENDED_6_CHALLENGE("challengeAlltimeKeepsDefended"),

    ALLTIME_TIME_PLAYED_1_CHALLENGE("challengeAlltimeTimePlayed"),
    ALLTIME_TIME_PLAYED_2_CHALLENGE("challengeAlltimeTimePlayed"),
    ALLTIME_TIME_PLAYED_3_CHALLENGE("challengeAlltimeTimePlayed"),
    ALLTIME_TIME_PLAYED_4_CHALLENGE("challengeAlltimeTimePlayed"),
    ALLTIME_TIME_PLAYED_5_CHALLENGE("challengeAlltimeTimePlayed"),
    ALLTIME_TIME_PLAYED_6_CHALLENGE("challengeAlltimeTimePlayed"),
    ALLTIME_MONSTER_KILLS_1_CHALLENGE("challengeAlltimeMonsterKills"),
    ALLTIME_MONSTER_KILLS_2_CHALLENGE("challengeAlltimeMonsterKills"),
    ALLTIME_MONSTER_KILLS_3_CHALLENGE("challengeAlltimeMonsterKills"),
    ALLTIME_MONSTER_KILLS_4_CHALLENGE("challengeAlltimeMonsterKills"),
    ALLTIME_MONSTER_KILLS_5_CHALLENGE("challengeAlltimeMonsterKills"),
    ALLTIME_MONSTER_KILLS_6_CHALLENGE("challengeAlltimeMonsterKills"),
    ALLTIME_RESOURCES_GATHERED_1_CHALLENGE("challengeAlltimeResourcesGathered"),
    ALLTIME_RESOURCES_GATHERED_2_CHALLENGE("challengeAlltimeResourcesGathered"),
    ALLTIME_RESOURCES_GATHERED_3_CHALLENGE("challengeAlltimeResourcesGathered"),
    ALLTIME_RESOURCES_GATHERED_4_CHALLENGE("challengeAlltimeResourcesGathered"),
    ALLTIME_RESOURCES_GATHERED_5_CHALLENGE("challengeAlltimeResourcesGathered"),
    ALLTIME_RESOURCES_GATHERED_6_CHALLENGE("challengeAlltimeResourcesGathered"),
    ALLTIME_CRATES_OPENED_1_CHALLENGE("challengeAlltimeCratesOpened"),
    ALLTIME_CRATES_OPENED_2_CHALLENGE("challengeAlltimeCratesOpened"),
    ALLTIME_CRATES_OPENED_3_CHALLENGE("challengeAlltimeCratesOpened"),
    ALLTIME_CRATES_OPENED_4_CHALLENGE("challengeAlltimeCratesOpened"),
    ALLTIME_CRATES_OPENED_5_CHALLENGE("challengeAlltimeCratesOpened"),
    ALLTIME_CRATES_OPENED_6_CHALLENGE("challengeAlltimeCratesOpened"),

    //Special Challenges
    SPECIAL_KILLS_CHALLENGE("challenge1337"),

    SPECIAL_CAMPS_CAPTURED_CHALLENGE("challengeWhyForTheGloryOfSatanOfCourse"),
    SPECIAL_OUTPOSTS_CAPTURED_CHALLENGE("challengeBlazeUm"),
    SPECIAL_KEEPS_CAPTURED_CHALLENGE("challengeSexyKeepYouGotThere"),

    SPECIAL_RESOURCES_GATHERED_CHALLENGE("challengeNice");

    private final String key;

    LegendsPlayerData(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
