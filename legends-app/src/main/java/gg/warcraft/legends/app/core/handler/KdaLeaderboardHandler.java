package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.event.EntityDeathEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;

import java.util.UUID;

public class KdaLeaderboardHandler {
    private static final String KILLS_LEADERBOARD_KEY_PREFIX = "leaderboard:kills:";
    private static final String DEATHS_LEADERBOARD_KEY_PREFIX = "leaderboard:deaths:";

    private final PlayerQueryService playerQueryService;
    private final GameQueryService gameQueryService;
    private final PersistenceService persistenceService;

    @Inject
    public KdaLeaderboardHandler(PlayerQueryService playerQueryService, GameQueryService gameQueryService,
                                 PersistenceService persistenceService) {
        this.playerQueryService = playerQueryService;
        this.gameQueryService = gameQueryService;
        this.persistenceService = persistenceService;
    }

    private String createLeaderboardKey(String keyPrefix) {
        int activeGameId = gameQueryService.getActiveGame().getId();
        return keyPrefix + activeGameId;
    }

    @Subscribe
    public void onEntityDeathEvent(EntityDeathEvent event) {
        UUID deadId = event.getEntityId();

        if (event.getEntityType() == EntityType.PLAYER) {
            // TODO implement lastDamageService (just a repo that listens to damage events and stores the last one,
            // TODO do we want to only store those that have a proper source? or if someone dies of other means after
            // TODO you hit them its too bad)
            // TODO make sure hunter pets are linked to the hunter

            // TODO what about killing mobs?


            String deathsKey = createLeaderboardKey(DEATHS_LEADERBOARD_KEY_PREFIX);
            persistenceService.incrSortedSet(deathsKey, deadId.toString(), 1);
        }
    }
}
