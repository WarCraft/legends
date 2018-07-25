package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.core.Game;

public interface GameQueryService {

    Game getGame(int id);

    Game getActiveGame();
}
