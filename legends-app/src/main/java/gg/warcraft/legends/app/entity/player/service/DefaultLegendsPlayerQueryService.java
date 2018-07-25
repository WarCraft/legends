package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerProfile;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerProfileRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.LazyLegendsPlayer;
import gg.warcraft.monolith.api.entity.EntityServerData;
import gg.warcraft.monolith.api.entity.player.service.PlayerServerAdapter;
import gg.warcraft.monolith.api.entity.service.EntityServerAdapter;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLegendsPlayerQueryService implements LegendsPlayerQueryService {
    private final LegendsPlayerProfileRepository repository;
    private final PlayerServerAdapter serverAdapter;
    private final EntityServerAdapter entityServerAdapter;

    @Inject
    public DefaultLegendsPlayerQueryService(LegendsPlayerProfileRepository repository, PlayerServerAdapter serverAdapter,
                                            EntityServerAdapter entityServerAdapter) {
        this.repository = repository;
        this.serverAdapter = serverAdapter;
        this.entityServerAdapter = entityServerAdapter;
    }

    @Override
    public LegendsPlayer getPlayer(UUID playerId) {
        LegendsPlayerProfile playerData = repository.get(playerId);
        EntityServerData playerServerData = entityServerAdapter.getEntityServerData(playerId);
        return new LazyLegendsPlayer(playerData, playerServerData);
    }

    @Override
    public Collection<LegendsPlayer> getOnlinePlayers() {
        return serverAdapter.getOnlinePlayers().stream()
                .map(this::getPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public UUID resolvePlayerId(String minecraftName) {
        return serverAdapter.resolvePlayerId(minecraftName);
    }
}
