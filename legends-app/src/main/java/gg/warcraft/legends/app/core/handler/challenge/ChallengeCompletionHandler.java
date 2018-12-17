package gg.warcraft.legends.app.core.handler.challenge;

import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.entity.player.event.PlayerStatisticChangedEvent;

public class ChallengeCompletionHandler {

    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {
        LegendsStatistic statistic = (LegendsStatistic) event.getStatistic();
        switch (statistic) {
            case DAILY_KILLS:
                break;
            case WAR_KILLS:
                break;
            case ALLTIME_KILLS:
                break;

            case DAILY_DEATHS:
                break;
            case WAR_DEATHS:
                break;
            case ALLTIME_DEATHS:
                break;

            case DAILY_ASSISTS:
                break;
            case WAR_ASSISTS:
                break;
            case ALLTIME_ASSISTS:
                break;

            case DAILY_DAMAGE_DONE:
                break;
            case WAR_DAMAGE_DONE:
                break;
            case ALLTIME_DAMAGE_DONE:
                break;

            case DAILY_HEALING_DONE:
                break;
            case WAR_HEALING_DONE:
                break;
            case ALLTIME_HEALING_DONE:
                break;

            case DAILY_DAMAGE_TAKEN:
                break;
            case WAR_DAMAGE_TAKEN:
                break;
            case ALLTIME_DAMAGE_TAKEN:
                break;

            case DAILY_CAMPS_CAPTURED:
                break;
            case WAR_CAMPS_NEUTRALIZED:
                break;
            case ALLTIME_CAMPS_DEFENDED:
                break;

            case DAILY_OUTPOSTS_CAPTURED:
                break;
            case WAR_OUTPOSTS_NEUTRALIZED:
                break;
            case ALLTIME_OUTPOSTS_DEFENDED:
                break;

            case DAILY_KEEPS_CAPTURED:
                break;
            case WAR_KEEPS_NEUTRALIZED:
                break;
            case ALLTIME_KEEPS_DEFENDED:
                break;

            case DAILY_TIME_PLAYED:
                break;
            case WAR_TIME_PLAYED:
                break;
            case ALLTIME_TIME_PLAYED:
                break;

            case DAILY_MONSTER_KILLS:
                break;
            case WAR_MONSTER_KILLS:
                break;
            case ALLTIME_MONSTER_KILLS:
                break;

            case DAILY_RESOURCES_GATHERED:
                break;
            case WAR_RESOURCES_GATHERED:
                break;
            case ALLTIME_RESOURCES_GATHERED:
                break;

            case DAILY_CHESTS_OPENED:
                break;
            case WAR_CHESTS_OPENED:
                break;
            case ALLTIME_CHESTS_OPENED:
                break;
        }
    }
}
