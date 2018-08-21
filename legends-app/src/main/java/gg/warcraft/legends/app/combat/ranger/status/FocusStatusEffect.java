package gg.warcraft.legends.app.combat.ranger.status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.combat.api.status.CombatStatusEffect;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.status.AbstractStatusEffect;

import java.util.UUID;

public class FocusStatusEffect extends AbstractStatusEffect implements CombatStatusEffect {
    private static final String NAME = "Focus";
    private static final String TYPE = "legends.marksman.focus";

    private final CombatValueModifier damageModifier;

    @Inject
    public FocusStatusEffect(CombatFactory combatFactory, @Assisted UUID entityId,
                             @Assisted float damageModifier) {
        super(entityId, NAME, TYPE, null);
        this.damageModifier =
                combatFactory.createCombatValueModifier(CombatValueModifierType.PERCENT, damageModifier, this);
    }

    @Override
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        event.addDamageModifier(damageModifier);
    }
}
