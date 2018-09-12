package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.chat.api.profile.service.ChatProfileCommandService;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;

import java.util.UUID;

public class PlayerRankChangedHandler {
    private final ChatProfileCommandService chatProfileCommandService;

    @Inject
    public PlayerRankChangedHandler(ChatProfileCommandService chatProfileCommandService) {
        this.chatProfileCommandService = chatProfileCommandService;
    }

    @Subscribe
    public void onPlayerRankChangedEvent(PlayerRankChangedEvent event) {
        UUID playerId = event.getPlayerId();
        Rank newRank = event.getNewRank();
        String name = newRank.getName();
        int prestige = newRank.getPrestige();

        String formattedName = name;
        if (prestige > 0) {
            String prestigePrefix = new String(new char[prestige]).replace("\0", "★");
            formattedName = prestigePrefix + " " + name;
        }
        chatProfileCommandService.setTag(playerId, formattedName, newRank.getColor());
    }
}
