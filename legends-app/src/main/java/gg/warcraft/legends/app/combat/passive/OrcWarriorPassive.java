package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.combat.api.status.CombatStatusEffect;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;

import java.util.UUID;

public class OrcWarriorPassive extends AbstractRacialPassive implements CombatStatusEffect {
    private static final String NAME = "Bloodlust";

    private final CombatValueModifier modifier;

    private boolean active;

    @Inject
    public OrcWarriorPassive(CombatFactory combatFactory, @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.modifier = combatFactory.createCombatValueModifier(CombatValueModifierType.PERCENT, 0.15f, this);
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        active = event.getNewPercentHealth() <= 0.5f;
    }

    @Override
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        if (active) {
            event.addDamageModifier(modifier);
        }
    }
}
