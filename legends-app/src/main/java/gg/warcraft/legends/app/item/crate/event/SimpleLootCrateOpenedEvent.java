package gg.warcraft.legends.app.item.crate.event;

import gg.warcraft.legends.api.item.crate.event.LootCrateOpenedEvent;
import gg.warcraft.monolith.api.entity.player.event.AbstractPlayerEvent;

import java.util.UUID;

public class SimpleLootCrateOpenedEvent extends AbstractPlayerEvent implements LootCrateOpenedEvent {

    public SimpleLootCrateOpenedEvent(UUID playerId) {
        super(playerId);
    }
}
