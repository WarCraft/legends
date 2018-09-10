package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.chat.api.profile.service.ChatProfileCommandService;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.event.PlayerFavorGainedEvent;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.team.Team;

public class PlayerRankEventHandler {
    private final RankQueryService rankQueryService;
    private final LegendsPlayerQueryService playerQueryService;
    private final ChatProfileCommandService chatProfileCommandService;

    @Inject
    public PlayerRankEventHandler(RankQueryService rankQueryService, LegendsPlayerQueryService playerQueryService,
                                  ChatProfileCommandService chatProfileCommandService) {
        this.rankQueryService = rankQueryService;
        this.playerQueryService = playerQueryService;
        this.chatProfileCommandService = chatProfileCommandService;
    }

    @Subscribe
    public void onPlayerFavorEarned(PlayerFavorGainedEvent event) {
        LegendsPlayer player = playerQueryService.getPlayer(event.getPlayerId());
        Team team = player.getTeam();
        int previousLifetimeFavor = event.getNewLifetime() - event.getAmount();
        Rank currentRank = rankQueryService.getRank(team, previousLifetimeFavor);
        Rank newRank = rankQueryService.getRank(team, event.getNewLifetime());
        if (!currentRank.equals(newRank)) {
            // FIXME should this instead be saved on the player as data and then fire an event of rank changed?
            // FIXME this way we can extract the rank without too much hassle when wanting to show it in menus etc
            chatProfileCommandService.setTag(player.getId(), newRank.toString(), newRank.getColor());
        }
    }
}
