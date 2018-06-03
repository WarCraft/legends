package gg.warcraft.legends.app.entity.player.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.event.PlayerFavorEarnedEvent;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;

public class PlayerRankEventHandler {
    private final LegendsPlayerQueryService playerQueryService;

    @Inject
    public PlayerRankEventHandler(LegendsPlayerQueryService playerQueryService) {
        this.playerQueryService = playerQueryService;
    }

    @Subscribe
    public void onPlayerFavorEarned(PlayerFavorEarnedEvent event) {
        LegendsPlayer player = playerQueryService.getPlayer(event.getPlayerId());
        int totalFavor = player.getTotalFavor();
        // TODO apply totalFavor to experience table
        Rank rank = player.getRank();
        // TODO check current rank vs supposed rank from table
        // TODO update rank if required and fire event
    }
}
