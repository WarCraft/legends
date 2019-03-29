package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;

import java.util.UUID;

public class HumanMagePassive extends AbstractRacialPassive {
    private static final String NAME = "Barrier";

    private final AbilityCommandService abilityCommandService;

    @Inject
    public HumanMagePassive(AbilityCommandService abilityCommandService, @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.abilityCommandService = abilityCommandService;
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        if (event.getNewPercentHealth() <= 0.5f) {
            abilityCommandService.castAbility("Barrier", getEntityId(), null);
        }
    }
}
