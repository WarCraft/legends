package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayerData;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.Faction;
import gg.warcraft.monolith.app.entity.player.SimplePlayerData;

import java.util.UUID;

public class SimpleLegendsPlayerData extends SimplePlayerData implements LegendsPlayerData {
    private final int goldCoins;
    private final int totalGoldCoins;
    private final int favor;
    private final int totalFavor;
    private final Rank rank;

    public SimpleLegendsPlayerData(UUID playerId, String minecraftName, String displayName, long timeOfConnect,
                                   long timeOfFirstConnect, long timePlayed, int timesVoted, Faction faction,
                                   int goldCoins, int totalGoldCoins, int favor, int totalFavor, Rank rank) {
        super(playerId, minecraftName, displayName, timeOfConnect, timeOfFirstConnect, timePlayed, timesVoted, faction);
        this.goldCoins = goldCoins;
        this.totalGoldCoins = totalGoldCoins;
        this.favor = favor;
        this.totalFavor = totalFavor;
        this.rank = rank;
    }

    @Override
    public int getGoldCoins() {
        return goldCoins;
    }

    @Override
    public int getTotalGoldCoins() {
        return totalGoldCoins;
    }

    @Override
    public int getFavor() {
        return favor;
    }

    @Override
    public int getTotalFavor() {
        return totalFavor;
    }

    @Override
    public Rank getRank() {
        return rank;
    }
}
