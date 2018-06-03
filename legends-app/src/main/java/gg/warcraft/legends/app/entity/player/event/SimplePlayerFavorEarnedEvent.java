package gg.warcraft.legends.app.entity.player.event;

import gg.warcraft.legends.api.entity.player.event.PlayerFavorEarnedEvent;
import gg.warcraft.monolith.app.entity.player.event.SimplePlayerEvent;

import java.util.UUID;

public class SimplePlayerFavorEarnedEvent extends SimplePlayerEvent implements PlayerFavorEarnedEvent {

    public SimplePlayerFavorEarnedEvent(UUID playerId) {
        super(playerId);
    }
}
