package gg.warcraft.legends.api.entity.player;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.player.Player;

public interface LegendsPlayer extends Player {

    int getGoldCoins();

    int getTotalGoldCoins();

    int getFavor();

    int getTotalFavor();

    Rank getRank();
}
