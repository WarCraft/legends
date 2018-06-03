package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.EntityServerData;
import gg.warcraft.monolith.app.entity.player.SimplePlayer;

public class SimpleLegendsPlayer extends SimplePlayer implements LegendsPlayer {
    private final LegendsPlayerData data;

    public SimpleLegendsPlayer(LegendsPlayerData data, EntityServerData serverData) {
        super(data, serverData);
        this.data = data;
    }

    @Override
    public int getGoldCoins() {
        return data.getGoldCoins();
    }

    @Override
    public int getTotalGoldCoins() {
        return data.getTotalGoldCoins();
    }

    @Override
    public int getFavor() {
        return data.getFavor();
    }

    @Override
    public int getTotalFavor() {
        return data.getTotalFavor();
    }

    @Override
    public Rank getRank() {
        return data.getRank();
    }
}
