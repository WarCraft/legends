package gg.warcraft.legends.api.core.game.service;

import gg.warcraft.legends.api.core.game.Game;

public interface GameRepository {

    Game getGame(int gameId);

    Game getActiveGame();

    Game getCurrentWar();

    int getCapturePoints(String objectiveType);

    void save(Game game);

    void saveActiveGame(int gameId);

    void saveCurrentWar(int gameId);

    void saveCapturePoints(String objectiveType, int points);
}
