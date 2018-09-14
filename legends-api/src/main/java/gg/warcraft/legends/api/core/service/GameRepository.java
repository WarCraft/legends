package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.core.Game;

public interface GameRepository {

    Game getGame(int gameId);

    Game getActiveGame();

    Game getCurrentWar();

    void save(Game game);

    void saveActiveGame(int gameId);

    void saveCurrentWar(int gameId);
}
