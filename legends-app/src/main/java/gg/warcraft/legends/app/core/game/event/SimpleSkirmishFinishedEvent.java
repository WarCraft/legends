package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.SkirmishFinishedEvent;

public class SimpleSkirmishFinishedEvent extends AbstractGameEvent implements SkirmishFinishedEvent {

    public SimpleSkirmishFinishedEvent(int gameId) {
        super(gameId);
    }
}
