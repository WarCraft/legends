package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.SimpleLegendsPlayer;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryServiceDecorator;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLegendsPlayerQueryService implements LegendsPlayerQueryService, PlayerQueryServiceDecorator {
    private final PlayerQueryService playerQueryService;

    @Inject
    public DefaultLegendsPlayerQueryService(PlayerQueryService playerQueryService) {
        this.playerQueryService = playerQueryService;
    }

    @Override
    public PlayerQueryService getPlayerQueryService() {
        return playerQueryService;
    }

    @Override
    public LegendsPlayer getPlayer(UUID playerId) {
        Player player = playerQueryService.getPlayer(playerId);
        return new SimpleLegendsPlayer(player);
    }

    @Override
    public List<LegendsPlayer> getOnlinePlayers() {
        return playerQueryService.getOnlinePlayers().stream()
                .map(SimpleLegendsPlayer::new)
                .collect(Collectors.toList());
    }
}
