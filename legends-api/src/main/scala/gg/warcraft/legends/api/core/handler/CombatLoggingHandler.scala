/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.monolith.api.core.event.Event

class CombatLoggingHandler extends Event.Handler {
  // TODO do we need this?
}

/*

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
 */
