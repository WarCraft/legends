package gg.warcraft.legends.app.entity.player.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.app.entity.player.SimpleLegendsPlayerData;
import gg.warcraft.monolith.api.entity.player.PlayerData;
import gg.warcraft.monolith.api.entity.player.event.PlayerPreConnectEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerDataRepository;

import java.util.UUID;

public class PlayerDataInitializationHandler {
    private final PlayerDataRepository playerDataRepository;

    @Inject
    public PlayerDataInitializationHandler(PlayerDataRepository playerDataRepository) {
        this.playerDataRepository = playerDataRepository;
    }

    @Subscribe
    public void onPlayerPreConnect(PlayerPreConnectEvent event) {
        UUID playerId = event.getPlayerId();
        String name = event.getName();
        PlayerData data = playerDataRepository.get(playerId);
        if (data == null) {
            long timeOfConnect = System.currentTimeMillis();
            data = new SimpleLegendsPlayerData(playerId, name, name, timeOfConnect, timeOfConnect,
                    0, 0, 0, 0);
            playerDataRepository.save(data);
        } else {
            // TODO set new time of connect
            // TODO check their minecraft name is still the same as in the data
        }
    }
}
