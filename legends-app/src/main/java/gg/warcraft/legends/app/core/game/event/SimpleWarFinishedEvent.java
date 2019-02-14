package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.WarFinishedEvent;

public class SimpleWarFinishedEvent extends AbstractGameEvent implements WarFinishedEvent {

    public SimpleWarFinishedEvent(int gameId) {
        super(gameId);
    }
}
