package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameQueryService;
import gg.warcraft.monolith.api.core.PersistenceService;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;

import java.util.UUID;

public class DamageLeaderboardsHandler {
    private static final String DAMAGE_DONE_LEADERBOARD_KEY_PREFIX = "leaderboard:damagedone:";
    private static final String DAMAGE_TAKEN_LEADERBOARD_KEY_PREFIX = "leaderboard:damagetaken:";

    private final PlayerQueryService playerQueryService;
    private final GameQueryService gameQueryService;
    private final PersistenceService persistenceService;

    @Inject
    public DamageLeaderboardsHandler(PlayerQueryService playerQueryService, GameQueryService gameQueryService,
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
    public void onEntityDamageEvent(EntityDamageEvent event) {
        double damage = event.getDamage().getModifiedValue();

        if (event.getEntityType() == EntityType.PLAYER) {
            String damageTakenKey = createLeaderboardKey(DAMAGE_TAKEN_LEADERBOARD_KEY_PREFIX);
            String damagedId = event.getEntityId().toString();
            persistenceService.incrSortedSet(damageTakenKey, damagedId, damage);
        }

        UUID damagerId = event.getDamage().getSource().getEntityId();
        if (damagerId != null) {
            Player damager = playerQueryService.getPlayer(damagerId);
            if (damager != null) {
                String damageDoneKey = createLeaderboardKey(DAMAGE_DONE_LEADERBOARD_KEY_PREFIX);
                persistenceService.incrSortedSet(damageDoneKey, damagerId.toString(), damage);
            }
        }
    }
}
