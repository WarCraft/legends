package gg.warcraft.legends.app.entity.player.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.app.entity.player.SimpleLegendsPlayerData;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.PlayerData;
import gg.warcraft.monolith.api.entity.player.event.PlayerPreConnectEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerDataRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;

import java.util.UUID;

public class PlayerDataInitializationHandler {
    private final PlayerDataRepository playerDataRepository;
    private final PlayerQueryService playerQueryService;

    @Inject
    public PlayerDataInitializationHandler(PlayerDataRepository playerDataRepository,
                                           PlayerQueryService playerQueryService) {
        this.playerDataRepository = playerDataRepository;
        this.playerQueryService = playerQueryService;
    }

    @Subscribe
    public void onPlayerPreConnect(PlayerPreConnectEvent event) {
        UUID playerId = event.getPlayerId();
        String name = event.getName();
        PlayerData data = playerDataRepository.get(playerId);
        if (data == null) {
            Player player = playerQueryService.getPlayer(playerId);
            data = new SimpleLegendsPlayerData(playerId, name, name, System.currentTimeMillis(),
                    System.currentTimeMillis(), 0, 0, 0, 0);
            playerDataRepository.save(data);
        }
    }
}
