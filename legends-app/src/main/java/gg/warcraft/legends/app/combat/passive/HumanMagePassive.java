package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.UUID;

public class HumanMagePassive extends AbstractRacialPassive {
    private static final String NAME = "Barrier";
    private static final int DURATION_IN_SECONDS = 5;
    private static final int ABSORPTION_LEVEL = 2;
    private static final long COOLDOWN_IN_MILLIS = 300 * TimeUtils.MILLIS_PER_SECOND;

    private final EntityCommandService entityCommandService;
    private final PotionEffect absorption;

    private long cooldown;

    @Inject
    public HumanMagePassive(EntityCommandService entityCommandService, TimeUtils timeUtils, CombatFactory combatFactory,
                            @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.entityCommandService = entityCommandService;
        Duration absorptionDuration = timeUtils.createDurationInSeconds(DURATION_IN_SECONDS);
        this.absorption =
                combatFactory.createPotionEffect(PotionEffectType.ABSORPTION, ABSORPTION_LEVEL, absorptionDuration);
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
