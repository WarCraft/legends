package gg.warcraft.legends.api.entity.player.event;

public interface PlayerGoldGainedEvent {

    int getAmount();

    int getNewCurrent();

    int getNewLifetime();
}
