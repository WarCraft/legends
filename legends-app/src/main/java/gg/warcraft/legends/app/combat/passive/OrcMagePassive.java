package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.UUID;

public class OrcMagePassive extends AbstractRacialPassive {
    private static final String NAME = "Flame Totem";
    private static final String ABILITY_NAME = "Flame Totem";
    private static final long COOLDOWN_IN_MILLIS = 40 * TimeUtils.MILLIS_PER_SECOND;

    private final AbilityCommandService abilityCommandService;

    private long cooldown;

    @Inject
    public OrcMagePassive(AbilityCommandService abilityCommandService, @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.abilityCommandService = abilityCommandService;
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        if (System.currentTimeMillis() >= cooldown) {
            if (event.getNewPercentHealth() <= 0.75) {
                cooldown = System.currentTimeMillis() + COOLDOWN_IN_MILLIS;
                abilityCommandService.castAbility(ABILITY_NAME, getEntityId(), null);
            }
        }
    }
}
