package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.event.PlayerFavorGainedEvent;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.rank.event.SimplePlayerRankChangedEvent;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.entity.player.service.PlayerServerAdapter;

import java.util.UUID;

public class PlayerRankChangeHandler {
    private final RankQueryService rankQueryService;
    private final LegendsPlayerQueryService playerQueryService;
    private final PlayerServerAdapter playerServerAdapter;
    private final EventService eventService;

    @Inject
    public PlayerRankChangeHandler(RankQueryService rankQueryService, LegendsPlayerQueryService playerQueryService,
                                   PlayerServerAdapter playerServerAdapter, EventService eventService) {
        this.rankQueryService = rankQueryService;
        this.playerQueryService = playerQueryService;
        this.playerServerAdapter = playerServerAdapter;
        this.eventService = eventService;
    }

    private void updatePlayerExperienceAndLevel(UUID playerId, float newLifetimeFavor, Rank rank) {
        float clippedNewLifetimeFavor = newLifetimeFavor - rank.getRequiredFavor();
        Rank nextRank = rankQueryService.getRank(rank.getLevel() + 1);

        float clippedRequiredFavor = nextRank.getRequiredFavor() - rank.getRequiredFavor();
        float newLevelProgression = clippedNewLifetimeFavor / clippedRequiredFavor;

        playerServerAdapter.setExperienceProgress(playerId, newLevelProgression);
        playerServerAdapter.setLevel(playerId, rank.getLevel());
    }

    @Subscribe
    public void onPlayerFavorEarned(PlayerFavorGainedEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        LegendsTeam team = player.getTeam();
        int newLifetimeFavor = event.getNewLifetime();
        int previousLifetimeFavor = newLifetimeFavor - event.getAmount();
        Rank currentRank = rankQueryService.getRank(team, previousLifetimeFavor);
        Rank newRank = rankQueryService.getRank(team, newLifetimeFavor);
        if (!currentRank.equals(newRank)) {
            PlayerRankChangedEvent rankChangedEvent = new SimplePlayerRankChangedEvent(playerId, currentRank, newRank);
            eventService.publish(rankChangedEvent);
        }

        updatePlayerExperienceAndLevel(playerId, newLifetimeFavor, newRank);
    }
}
