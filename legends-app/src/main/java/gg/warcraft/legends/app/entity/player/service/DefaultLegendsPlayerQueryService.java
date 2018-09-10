package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsEntityFactory;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryServiceDecorator;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLegendsPlayerQueryService implements LegendsPlayerQueryService, PlayerQueryServiceDecorator {
    private final PlayerQueryService playerQueryService;
    private final LegendsEntityFactory legendsEntityFactory;

    @Inject
    public DefaultLegendsPlayerQueryService(PlayerQueryService playerQueryService,
                                            LegendsEntityFactory legendsEntityFactory) {
        this.playerQueryService = playerQueryService;
        this.legendsEntityFactory = legendsEntityFactory;
    }

    @Override
    public PlayerQueryService getPlayerQueryService() {
        return playerQueryService;
    }

    @Override
    public LegendsPlayer getPlayer(UUID playerId) {
        Player player = playerQueryService.getPlayer(playerId);
        return legendsEntityFactory.createLegendsPlayer(player);
    }

    @Override
    public List<LegendsPlayer> getOnlinePlayers() {
        return playerQueryService.getOnlinePlayers().stream()
                .map(legendsEntityFactory::createLegendsPlayer)
                .collect(Collectors.toList());
    }
}
