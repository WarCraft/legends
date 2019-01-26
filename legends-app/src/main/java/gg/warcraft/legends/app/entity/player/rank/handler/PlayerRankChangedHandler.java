package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.chat.api.profile.service.ChatProfileCommandService;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.location.Location;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import java.util.UUID;

public class PlayerRankChangedHandler {
    private final PlayerQueryService playerQueryService;
    private final ChatProfileCommandService chatProfileCommandService;
    private final WorldCommandService worldCommandService;

    @Inject
    public PlayerRankChangedHandler(PlayerQueryService playerQueryService,
                                    ChatProfileCommandService chatProfileCommandService,
                                    WorldCommandService worldCommandService) {
        this.playerQueryService = playerQueryService;
        this.chatProfileCommandService = chatProfileCommandService;
        this.worldCommandService = worldCommandService;
    }

    @Subscribe
    public void onPlayerRankChangedEvent(PlayerRankChangedEvent event) {
        UUID playerId = event.getPlayerId();
        Player player = playerQueryService.getPlayer(playerId);
        if (player == null) {
            return;
        }

        Rank newRank = event.getNewRank();
        chatProfileCommandService.setTag(playerId, newRank.getFormattedName(), newRank.getColor());

        Location location = player.getLocation();
        worldCommandService.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
    }
}
