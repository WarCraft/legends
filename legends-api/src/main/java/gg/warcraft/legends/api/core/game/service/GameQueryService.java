package gg.warcraft.legends.api.core.game.service;

import gg.warcraft.legends.api.core.game.Game;

public interface GameQueryService {

    Game getGame(int id);

    Game getActiveGame();

    Game getCurrentWar();
}
