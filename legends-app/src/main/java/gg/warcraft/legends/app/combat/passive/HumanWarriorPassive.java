package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.Ability;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.event.EntityPreFatalDamageEvent;

import java.util.UUID;

public class HumanWarriorPassive extends AbstractRacialPassive {
    private static final String NAME = "Last Stand";
    private static final String ABILITY_NAME = "Last Stand";

    private final AbilityCommandService abilityCommandService;

    @Inject
    public HumanWarriorPassive(AbilityCommandService abilityCommandService,
                               @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.abilityCommandService = abilityCommandService;
    }

    @Override
    public void onEntityPreFatalDamageEvent(EntityPreFatalDamageEvent event) {
        Ability ability = abilityCommandService.castAbility(ABILITY_NAME, getEntityId(), null);
        if (ability != null) {
            event.cancel();
        }
    }
}
