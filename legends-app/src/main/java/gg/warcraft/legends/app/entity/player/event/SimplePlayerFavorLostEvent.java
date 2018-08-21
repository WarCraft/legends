package gg.warcraft.legends.app.entity.player.event;

import gg.warcraft.legends.api.entity.player.event.PlayerFavorLostEvent;
import gg.warcraft.monolith.api.entity.player.event.AbstractPlayerEvent;

import java.util.UUID;

public class SimplePlayerFavorLostEvent extends AbstractPlayerEvent implements PlayerFavorLostEvent {
    private final int amount;
    private final int newCurrent;

    public SimplePlayerFavorLostEvent(UUID playerId, int amount, int newCurrent) {
        super(playerId);
        this.amount = amount;
        this.newCurrent = newCurrent;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getNewCurrent() {
        return newCurrent;
    }
}
