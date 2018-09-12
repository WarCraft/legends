package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.event.PlayerFavorGainedEvent;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.rank.event.SimplePlayerRankChangedEvent;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.UUID;

public class PlayerRankChangeHandler {
    private final RankQueryService rankQueryService;
    private final LegendsPlayerQueryService playerQueryService;
    private final EventService eventService;

    @Inject
    public PlayerRankChangeHandler(RankQueryService rankQueryService, LegendsPlayerQueryService playerQueryService,
                                   EventService eventService) {
        this.rankQueryService = rankQueryService;
        this.playerQueryService = playerQueryService;
        this.eventService = eventService;
    }

    @Subscribe
    public void onPlayerFavorEarned(PlayerFavorGainedEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Team team = player.getTeam();
        int previousLifetimeFavor = event.getNewLifetime() - event.getAmount();
        Rank currentRank = rankQueryService.getRank(team, previousLifetimeFavor);
        Rank newRank = rankQueryService.getRank(team, event.getNewLifetime());
        if (!currentRank.equals(newRank)) {
            PlayerRankChangedEvent rankChangedEvent = new SimplePlayerRankChangedEvent(playerId, currentRank, newRank);
            eventService.publish(rankChangedEvent);
        }
    }
}
