package gg.warcraft.legends.app.entity.player.event;

import gg.warcraft.legends.api.entity.player.event.PlayerGoldLostEvent;
import gg.warcraft.monolith.app.entity.player.event.SimplePlayerEvent;

import java.util.UUID;

public class SimplePlayerGoldLostEvent extends SimplePlayerEvent implements PlayerGoldLostEvent {
    private final int amount;
    private final int newCurrent;

    public SimplePlayerGoldLostEvent(UUID playerId, int amount, int newCurrent) {
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
