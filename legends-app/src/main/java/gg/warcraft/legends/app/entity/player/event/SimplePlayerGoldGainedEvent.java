package gg.warcraft.legends.app.entity.player.event;

import gg.warcraft.legends.api.entity.player.event.PlayerGoldGainedEvent;
import gg.warcraft.monolith.api.entity.player.event.AbstractPlayerEvent;

import java.util.UUID;

public class SimplePlayerGoldGainedEvent extends AbstractPlayerEvent implements PlayerGoldGainedEvent {
    private final int amount;
    private final int newCurrent;
    private final int newLifetime;

    public SimplePlayerGoldGainedEvent(UUID playerId, int amount, int newCurrent, int newLifetime) {
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
