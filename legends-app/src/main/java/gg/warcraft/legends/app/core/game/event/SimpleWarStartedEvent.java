package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.WarStartedEvent;

public class SimpleWarStartedEvent extends AbstractGameEvent implements WarStartedEvent {

    public SimpleWarStartedEvent(int gameId) {
        super(gameId);
    }
}
