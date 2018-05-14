package gg.warcraft.legends.data;

import gg.warcraft.monolith.api.data.PlayerData;

public interface LegendsPlayerData extends PlayerData {

    int getGoldCoins();

    void addGoldCoins(int amount);

    void subtractGoldCoins(int amount);

    int getFavor();

    void addFavor(int amount);

    void subtractFavor(int amount);
}
