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

    private float calculateExperienceProgress(float newLifetimeFavor, Rank newRank) {
        float clippedNewLifetimeFavor = newLifetimeFavor - newRank.getRequiredFavor();
        Rank nextRank = rankQueryService.getRank(newRank.getLevel() + 1);
        if (nextRank == null) {
            return 0; // TODO this will give issues once players prestige
        }

        float clippedRequiredFavor = nextRank.getRequiredFavor() - newRank.getRequiredFavor();
        return clippedNewLifetimeFavor / clippedRequiredFavor;
    }

    @Subscribe
    public void onPlayerFavorEarned(PlayerFavorGainedEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        LegendsTeam team = player.getTeam();
        int previousLifetimeFavor = event.getNewLifetime() - event.getAmount();
        Rank currentRank = rankQueryService.getRank(team, previousLifetimeFavor);
        Rank newRank = rankQueryService.getRank(team, event.getNewLifetime());
        if (!currentRank.equals(newRank)) {
            PlayerRankChangedEvent rankChangedEvent = new SimplePlayerRankChangedEvent(playerId, currentRank, newRank);
            eventService.publish(rankChangedEvent);
        }

        float newLevelProgression = calculateExperienceProgress(event.getNewLifetime(), newRank);
        playerServerAdapter.setExperienceProgress(playerId, newLevelProgression);
    }
}
