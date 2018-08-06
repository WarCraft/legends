package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;
import gg.warcraft.monolith.app.world.SimplePotionEffect;

import java.util.UUID;

public class HumanMagePassive extends AbstractRacialPassive {
    private static final int DURATION_IN_SECONDS = 5;
    private static final int ABSORPTION_LEVEL = 2;
    private static final long COOLDOWN_IN_MILLIS = 300 * TimeUtils.MILLIS_PER_SECOND;

    private final EntityCommandService entityCommandService;
    private final PotionEffect absorption;

    private long cooldown;

    @Inject
    public HumanMagePassive(EntityCommandService entityCommandService, TimeUtils timeUtils, @Assisted UUID entityId) {
        super(entityId, null);
        this.entityCommandService = entityCommandService;
        Duration absorptionDuration = timeUtils.createDurationInSeconds(DURATION_IN_SECONDS);
        this.absorption = new SimplePotionEffect(PotionEffectType.ABSORPTION, ABSORPTION_LEVEL, absorptionDuration);
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        if (System.currentTimeMillis() >= cooldown) {
            if (event.getNewPercentHealth() <= 0.5f) {
                cooldown = System.currentTimeMillis() + COOLDOWN_IN_MILLIS;
                entityCommandService.addPotionEffect(event.getEntityId(), absorption);
            }
        }
    }
}
