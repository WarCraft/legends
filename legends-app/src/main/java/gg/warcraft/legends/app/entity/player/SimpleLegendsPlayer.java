package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.monolith.api.entity.player.PlayerServerData;
import gg.warcraft.monolith.app.entity.player.SimplePlayer;

public class SimpleLegendsPlayer extends SimplePlayer implements LegendsPlayer {
    private final LegendsPlayerData data;

    public SimpleLegendsPlayer(LegendsPlayerData data, PlayerServerData serverData) {
        super(data, serverData);
        this.data = data;
    }

    @Override
    public int getGoldCoins() {
        return data.getGoldCoins();
    }

    @Override
    public int getFavor() {
        return data.getFavor();
    }
}
