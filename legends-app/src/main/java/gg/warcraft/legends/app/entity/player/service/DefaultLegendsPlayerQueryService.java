package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.LazyLegendsPlayer;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeQueryService;
import gg.warcraft.monolith.api.entity.player.service.PlayerProfileRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerServerAdapter;
import gg.warcraft.monolith.api.entity.status.service.StatusQueryService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLegendsPlayerQueryService implements LegendsPlayerQueryService {
    private final PlayerProfileRepository playerProfileRepository;
    private final AttributeQueryService attributeQueryService;
    private final StatusQueryService statusQueryService;
    private final PlayerServerAdapter playerServerAdapter;

    @Inject
    public DefaultLegendsPlayerQueryService(PlayerProfileRepository playerProfileRepository,
                                            AttributeQueryService attributeQueryService, StatusQueryService statusQueryService,
                                            PlayerServerAdapter playerServerAdapter) {
        this.playerProfileRepository = playerProfileRepository;
        this.attributeQueryService = attributeQueryService;
        this.statusQueryService = statusQueryService;
        this.playerServerAdapter = playerServerAdapter;
    }

    @Override
    public LegendsPlayer getPlayer(UUID playerId) {
        return new LazyLegendsPlayer(
                () -> playerProfileRepository.get(playerId),
                () -> playerServerAdapter.getPlayerServerData(playerId),
                () -> attributeQueryService.getAttributes(playerId),
                () -> statusQueryService.getStatus(playerId));
    }

    @Override
    public List<LegendsPlayer> getOnlinePlayers() {
        return playerServerAdapter.getOnlinePlayers().stream()
                .map(this::getPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public UUID resolvePlayerId(String minecraftName) {
        return playerServerAdapter.resolvePlayerId(minecraftName);
    }
}
