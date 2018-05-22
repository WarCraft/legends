package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerDataRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.SimpleLegendsPlayer;
import gg.warcraft.monolith.api.entity.player.PlayerServerData;
import gg.warcraft.monolith.api.entity.player.adapter.PlayerServerAdapter;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultLegendsPlayerQueryService implements LegendsPlayerQueryService {
    private final LegendsPlayerDataRepository repository;
    private final PlayerServerAdapter serverAdapter;

    @Inject
    public DefaultLegendsPlayerQueryService(LegendsPlayerDataRepository repository, PlayerServerAdapter serverAdapter) {
        this.repository = repository;
        this.serverAdapter = serverAdapter;
    }

    @Override
    public LegendsPlayer getPlayer(UUID playerId) {
        LegendsPlayerData playerData = repository.get(playerId);
        PlayerServerData playerServerData = serverAdapter.getPlayerServerData(playerId);
        return new SimpleLegendsPlayer(playerData, playerServerData);
    }

    @Override
    public Collection<LegendsPlayer> getOnlinePlayers() {
        return serverAdapter.getOnlinePlayers().stream()
                .map(this::getPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isNameAvailable(String name) {
        return serverAdapter.isNameAvailable(name);
    }

    @Override
    public UUID resolvePlayerId(String minecraftName) {
        return serverAdapter.resolvePlayerId(minecraftName);
    }
}
