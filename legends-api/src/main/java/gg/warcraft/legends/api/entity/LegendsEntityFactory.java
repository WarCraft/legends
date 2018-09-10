package gg.warcraft.legends.api.entity;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.monolith.api.entity.player.Player;

public interface LegendsEntityFactory {

    LegendsPlayer createLegendsPlayer(Player player);
}
