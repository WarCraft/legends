package gg.warcraft.legends.api.entity.player.rank.event;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.player.event.PlayerEvent;

public interface PlayerRankChangedEvent extends PlayerEvent {

    Rank getPreviousRank();

    Rank getNewRank();
}
