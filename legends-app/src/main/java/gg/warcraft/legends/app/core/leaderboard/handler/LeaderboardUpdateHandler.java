package gg.warcraft.legends.app.core.leaderboard.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardCommandService;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.monolith.api.entity.player.event.PlayerStatisticChangedEvent;

import java.util.EnumSet;

import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_CAMPS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_CAMPS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_CRATES_OPENED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_DAMAGE_DONE;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_FAVOR_GAINED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_GOLD_GAINED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_KEEPS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_KEEPS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_KILLS;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_MONSTER_KILLS;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_RESOURCES_GATHERED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.ALLTIME_TIME_PLAYED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_CAMPS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_CAMPS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_CRATES_OPENED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_DAMAGE_DONE;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_FAVOR_GAINED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_GOLD_GAINED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_KEEPS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_KEEPS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_KILLS;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_MONSTER_KILLS;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_OUTPOSTS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_OUTPOSTS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_RESOURCES_GATHERED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.DAILY_TIME_PLAYED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_CAMPS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_CAMPS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_CRATES_OPENED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_DAMAGE_DONE;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_FAVOR_GAINED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_GOLD_GAINED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_KEEPS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_KEEPS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_KILLS;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_MONSTER_KILLS;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_OUTPOSTS_CAPTURED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_OUTPOSTS_DEFENDED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_RESOURCES_GATHERED;
import static gg.warcraft.legends.api.entity.player.LegendsStatistic.WARRY_TIME_PLAYED;

public class LeaderboardUpdateHandler {
    private static final EnumSet<LegendsStatistic> STATISTICS_TO_TRACK;

    static {
        STATISTICS_TO_TRACK = EnumSet.of(
                // Combat Statistics
                DAILY_KILLS,
                WARRY_KILLS,
                ALLTIME_KILLS,

                DAILY_DAMAGE_DONE,
                WARRY_DAMAGE_DONE,
                ALLTIME_DAMAGE_DONE,

                // Siege Statistics
                DAILY_CAMPS_CAPTURED,
                WARRY_CAMPS_CAPTURED,
                ALLTIME_CAMPS_CAPTURED,

                DAILY_CAMPS_DEFENDED,
                WARRY_CAMPS_DEFENDED,
                ALLTIME_CAMPS_DEFENDED,

                DAILY_OUTPOSTS_CAPTURED,
                WARRY_OUTPOSTS_CAPTURED,
                ALLTIME_OUTPOSTS_CAPTURED,

                DAILY_OUTPOSTS_DEFENDED,
                WARRY_OUTPOSTS_DEFENDED,
                ALLTIME_OUTPOSTS_DEFENDED,

                DAILY_KEEPS_CAPTURED,
                WARRY_KEEPS_CAPTURED,
                ALLTIME_KEEPS_CAPTURED,

                DAILY_KEEPS_DEFENDED,
                WARRY_KEEPS_DEFENDED,
                ALLTIME_KEEPS_DEFENDED,

                // Currency Statistics
                DAILY_FAVOR_GAINED,
                WARRY_FAVOR_GAINED,
                ALLTIME_FAVOR_GAINED,

                DAILY_GOLD_GAINED,
                WARRY_GOLD_GAINED,
                ALLTIME_GOLD_GAINED,

                // Misc Statistics
                DAILY_TIME_PLAYED,
                WARRY_TIME_PLAYED,
                ALLTIME_TIME_PLAYED,

                DAILY_MONSTER_KILLS,
                WARRY_MONSTER_KILLS,
                ALLTIME_MONSTER_KILLS,

                DAILY_RESOURCES_GATHERED,
                WARRY_RESOURCES_GATHERED,
                ALLTIME_RESOURCES_GATHERED,

                DAILY_CRATES_OPENED,
                WARRY_CRATES_OPENED,
                ALLTIME_CRATES_OPENED);
    }

    private final LeaderboardCommandService leaderboardCommandService;

    @Inject
    public LeaderboardUpdateHandler(LeaderboardCommandService leaderboardCommandService) {
        this.leaderboardCommandService = leaderboardCommandService;
    }

    @Subscribe
    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {
        if (event.getStatistic() instanceof LegendsStatistic
                && STATISTICS_TO_TRACK.contains(event.getStatistic())) {
            LegendsStatistic statistic = (LegendsStatistic) event.getStatistic();
            leaderboardCommandService.updateEntry(statistic, event.getPlayerId(), event.getNewCurrent());
        }
    }
}
