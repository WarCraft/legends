package gg.warcraft.legends.entity.data;

import gg.warcraft.monolith.api.entity.player.PlayerData;

public interface LegendsPlayerData extends PlayerData {

    int getGoldCoins();

    void addGoldCoins(int amount);

    void subtractGoldCoins(int amount);

    int getFavor();

    void addFavor(int amount);

    void subtractFavor(int amount);
}
