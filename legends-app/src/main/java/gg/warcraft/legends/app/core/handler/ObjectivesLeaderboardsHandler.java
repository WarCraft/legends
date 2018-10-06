package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.siege.api.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveDefendedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveNeutralizedEvent;

public class ObjectivesLeaderboardsHandler {

    @Subscribe
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        // TODO store which players contributed to objective events on the event
    }

    @Subscribe
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {

    }

    @Subscribe
    public void onObjectiveDefendedEvent(ObjectiveDefendedEvent event) {

    }
}
