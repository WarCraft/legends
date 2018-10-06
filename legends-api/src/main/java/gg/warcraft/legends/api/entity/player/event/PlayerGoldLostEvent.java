package gg.warcraft.legends.api.entity.player.event;

import gg.warcraft.monolith.api.entity.player.event.PlayerEvent;

public interface PlayerGoldLostEvent extends PlayerEvent {

    int getAmount();

    int getNewCurrent();
}
