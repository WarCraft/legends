package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.combat.api.status.CombatStatusEffect;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.UUID;

public class OrcWarriorPassive extends AbstractRacialPassive implements CombatStatusEffect {
    private static final String NAME = "Bloodlust";

    private final EntityCommandService entityCommandService;
    private final PotionEffect potionEffect;
    private final CombatValueModifier modifier;

    private boolean active;

    @Inject
    public OrcWarriorPassive(EntityCommandService entityCommandService,
                             CombatFactory combatFactory, TimeUtils timeUtils,
                             @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.entityCommandService = entityCommandService;
        Duration forever = timeUtils.createDurationInTicks(Integer.MAX_VALUE);
        this.potionEffect = combatFactory.createPotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, forever);
        this.modifier = combatFactory.createCombatValueModifier(CombatValueModifierType.PERCENT, 0.15f, this);
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        if (event.getNewPercentHealth() <= 0.5f) {
            entityCommandService.addPotionEffect(getEntityId(), potionEffect);
            active = true;
        } else {
            entityCommandService.removePotionEffect(getEntityId(), PotionEffectType.INCREASE_DAMAGE);
            active = false;
        }
    }

    @Override
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        if (active) {
            event.addDamageModifier(modifier);
        }
    }
}
