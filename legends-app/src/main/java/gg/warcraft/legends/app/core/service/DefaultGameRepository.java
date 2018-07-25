package gg.warcraft.legends.app.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.Game;
import gg.warcraft.legends.api.core.service.GameRepository;

import javax.jnlp.PersistenceService;

@Singleton
public class DefaultGameRepository implements GameRepository {
    private final PersistenceService persistenceService;

    @Inject
    public DefaultGameRepository(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public Game getGame(int id) {
        return null;
    }

    @Override
    public Game getActiveGame() {
        return null;
    }

    @Override
    public void save(Game game) {

    }
}
