package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.service.SpecializationQueryService;
import gg.warcraft.combat.api.status.CombatStatusEffect;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;

import java.util.Random;
import java.util.UUID;

public class CritPassive extends AbstractRacialPassive implements CombatStatusEffect {
    private static final String EFFECT_NAME = "Crit";
    private static final float CRIT_MODIFIER = 0.5f;
    private static final Random random;

    static {
        random = new Random(System.currentTimeMillis());
    }

    private final EntityQueryService entityQueryService;
    private final SpecializationQueryService specializationQueryService;
    private final CombatFactory combatFactory;

    @Inject
    public CritPassive(EntityQueryService entityQueryService,
                       SpecializationQueryService specializationQueryService,
                       CombatFactory combatFactory, @Assisted UUID entityId) {
        super(entityId, EFFECT_NAME, null);
        this.entityQueryService = entityQueryService;
        this.specializationQueryService = specializationQueryService;
        this.combatFactory = combatFactory;
    }

    @Override
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        Specialization specialization = specializationQueryService.getSpecialization(getEntityId());
        if (specialization == null) {
            return;
        }

        Entity entity = entityQueryService.getEntity(getEntityId());
        float crit = entity.getAttributes().getValue(LegendsAttribute.CRIT);
        float critChance = crit * 0.005f;
        if (random.nextFloat() <= critChance) {
            CombatValueModifier damageModifier =
                    combatFactory.createCombatValueModifier(CombatValueModifierType.PERCENT, CRIT_MODIFIER, this);
            event.addDamageModifier(damageModifier);
        }
    }
}
