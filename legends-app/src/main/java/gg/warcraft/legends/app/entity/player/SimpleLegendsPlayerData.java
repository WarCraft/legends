package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.monolith.app.entity.player.SimplePlayerData;

import java.util.UUID;

public class SimpleLegendsPlayerData extends SimplePlayerData implements LegendsPlayerData {
    private final int goldCoins;
    private final int favor;

    public SimpleLegendsPlayerData(UUID playerId, String minecraftName, String displayName, long timeOfConnect,
                                   long timeOfFirstConnect, long timePlayed, int timesVoted, int goldCoins, int favor) {
        super(playerId, minecraftName, displayName, timeOfConnect, timeOfFirstConnect, timePlayed, timesVoted);
        this.goldCoins = goldCoins;
        this.favor = favor;
    }

    @Override
    public int getGoldCoins() {
        return goldCoins;
    }

    @Override
    public int getFavor() {
        return favor;
    }
}
