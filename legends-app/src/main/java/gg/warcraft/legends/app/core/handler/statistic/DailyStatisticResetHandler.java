package gg.warcraft.legends.app.core.handler.statistic;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;

import java.util.UUID;

public class DailyStatisticResetHandler {

    private void resetDailies(UUID playerId) {

    }

    private void resetWarries(UUID playerId) {

    }

    public void onDayChangedEvent() {
        // TODO
    }

    public void onWarStartedEvent() {
        // TODO
        // TODO also add a WarResumedEvent and WarFinishEvent?
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {

    }
}
