package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayerProfile;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerProfileRepository;
import gg.warcraft.monolith.api.core.PersistenceService;

import java.util.UUID;

public class DefaultLegendsPlayerProfileRepository implements LegendsPlayerProfileRepository {
    private static final String LEGENDS_PROFILE_KEY_PREFIX = "legendsplayer:";

    private final PersistenceService persistenceService;

    @Inject
    public DefaultLegendsPlayerProfileRepository(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public LegendsPlayerProfile get(UUID playerId) {
        return null;
    }

    @Override
    public void save(LegendsPlayerProfile profile) {

    }
}
