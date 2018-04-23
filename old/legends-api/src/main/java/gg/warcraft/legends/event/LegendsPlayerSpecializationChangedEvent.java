package gg.warcraft.legends.event;

import gg.warcraft.legends.player.LegendsPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LegendsPlayerSpecializationChangedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final LegendsPlayer player;

    public LegendsPlayerSpecializationChangedEvent(LegendsPlayer player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public LegendsPlayer getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
