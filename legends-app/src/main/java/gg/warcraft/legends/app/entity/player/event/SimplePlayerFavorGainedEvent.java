package gg.warcraft.legends.app.entity.player.event;

import gg.warcraft.legends.api.entity.player.event.PlayerFavorGainedEvent;
import gg.warcraft.monolith.app.entity.player.event.SimplePlayerEvent;

import java.util.UUID;

public class SimplePlayerFavorGainedEvent extends SimplePlayerEvent implements PlayerFavorGainedEvent {
    private final int amount;
    private final int newCurrent;
    private final int newLifetime;

    public SimplePlayerFavorGainedEvent(UUID playerId, int amount, int newCurrent, int newLifetime) {
        super(playerId);
        this.amount = amount;
        this.newCurrent = newCurrent;
        this.newLifetime = newLifetime;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getNewCurrent() {
        return newCurrent;
    }

    @Override
    public int getNewLifetime() {
        return newLifetime;
    }
}
