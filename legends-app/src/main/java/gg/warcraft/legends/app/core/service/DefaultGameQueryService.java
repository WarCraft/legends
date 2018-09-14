package gg.warcraft.legends.app.core.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.Game;
import gg.warcraft.legends.api.core.service.GameQueryService;
import gg.warcraft.legends.api.core.service.GameRepository;

public class DefaultGameQueryService implements GameQueryService {
    private final GameRepository gameRepository;

    @Inject
    public DefaultGameQueryService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game getGame(int id) {
        return gameRepository.getGame(id);
    }

    @Override
    public Game getActiveGame() {
        return gameRepository.getActiveGame();
    }

    @Override
    public Game getCurrentWar() {
        return gameRepository.getCurrentWar();
    }
}
