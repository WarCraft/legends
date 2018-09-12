package gg.warcraft.legends.app.entity.player.rank.event;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;
import gg.warcraft.monolith.api.entity.player.event.AbstractPlayerEvent;

import java.util.UUID;

public class SimplePlayerRankChangedEvent extends AbstractPlayerEvent implements PlayerRankChangedEvent {
    private final Rank previousRank;
    private final Rank newRank;

    public SimplePlayerRankChangedEvent(UUID playerId, Rank previousRank, Rank newRank) {
        super(playerId);
        this.previousRank = previousRank;
        this.newRank = newRank;
    }

    @Override
    public Rank getPreviousRank() {
        return previousRank;
    }

    @Override
    public Rank getNewRank() {
        return newRank;
    }
}
