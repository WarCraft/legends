package gg.warcraft.legends.app.leaderboard.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.monolith.api.entity.player.event.PlayerStatisticChangedEvent;

public class LeaderboardUpdateHandler {

    @Subscribe
    public void onPlayerStatisticChangedEvent(PlayerStatisticChangedEvent event) {

    }
}
