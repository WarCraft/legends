package gg.warcraft.legends.app.combat.ranger.status;

import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.combat.api.status.CombatStatusEffect;
import gg.warcraft.monolith.api.combat.CombatSource;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.app.combat.SimpleCombatSource;
import gg.warcraft.monolith.app.combat.value.SimpleCombatValueModifier;
import gg.warcraft.monolith.app.entity.status.AbstractStatusEffect;

import java.util.UUID;

public class FocusStatusEffect extends AbstractStatusEffect implements CombatStatusEffect {
    private static final String NAME = "Focus";
    private final CombatValueModifier damageModifier;

    public FocusStatusEffect(@Assisted UUID entityId, @Assisted float damageModifier) {
        super(entityId, null);
        CombatSource source = new SimpleCombatSource(NAME, entityId);
        this.damageModifier = new SimpleCombatValueModifier(CombatValueModifierType.PERCENT, damageModifier, source);
    }

    @Override
    public String getType() {
        return "legends.marksman.focus";
    }

    @Override
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        event.addDamageModifier(damageModifier);
    }
}
