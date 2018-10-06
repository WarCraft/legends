package gg.warcraft.legends.api.entity.player.event;

import gg.warcraft.monolith.api.entity.player.event.PlayerEvent;

public interface PlayerGoldGainedEvent extends PlayerEvent {

    int getAmount();

    int getNewCurrent();

    int getNewLifetime();
}
