package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.core.Game;

public interface GameRepository {

    Game getGame(int id);

    Game getActiveGame();

    void save(Game game);
}
