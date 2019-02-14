package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.SkirmishStartedEvent;

public class SimpleSkirmishStartedEvent extends AbstractGameEvent implements SkirmishStartedEvent {

    public SimpleSkirmishStartedEvent(int gameId) {
        super(gameId);
    }
}
