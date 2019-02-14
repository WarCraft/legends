package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.WarResumedEvent;

public class SimpleWarResumedEvent extends AbstractGameEvent implements WarResumedEvent {

    public SimpleWarResumedEvent(int gameId) {
        super(gameId);
    }
}
