package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerDisconnectEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class CombatLoggingHandler {
    private static final long TEN_TICKS_IN_SECONDS = TimeUtils.TICKS_PER_SECOND * 10;

    private final EntityCommandService entityCommandService;
    private final Map<UUID, Long> lastDamaged;

    @Inject
    public CombatLoggingHandler(EntityCommandService entityCommandService) {
        this.entityCommandService = entityCommandService;
        this.lastDamaged = new HashMap<>();
    }

    @Subscribe
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            lastDamaged.put(event.getEntityId(), System.currentTimeMillis());
        }
    }

    @Subscribe
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        UUID playerId = event.getPlayerId();
        long playerLastDamaged = lastDamaged.getOrDefault(playerId, 0L);
        long timeSinceLastDamage = System.currentTimeMillis() - playerLastDamaged;
        if (timeSinceLastDamage <= TEN_TICKS_IN_SECONDS) {
            entityCommandService.kill(playerId);
        }
    }
}
