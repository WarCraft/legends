package gg.warcraft.legends.objective.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PointsTalliedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final int humanScore;
    private final int orcScore;

    public PointsTalliedEvent(int humanScore, int orcScore) {
        this.humanScore = humanScore;
        this.orcScore = orcScore;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getHumanScore() {
        return humanScore;
    }

    public int getOrcScore() {
        return orcScore;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
