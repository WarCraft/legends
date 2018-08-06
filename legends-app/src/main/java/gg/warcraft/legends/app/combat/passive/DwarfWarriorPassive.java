package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.combat.CombatSource;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;
import gg.warcraft.monolith.api.entity.event.EntityPreDamageEvent;
import gg.warcraft.monolith.app.combat.SimpleCombatSource;
import gg.warcraft.monolith.app.combat.value.SimpleCombatValueModifier;

import java.util.UUID;

public class DwarfWarriorPassive extends AbstractRacialPassive {
    private static final String NAME = "Sturdiness";

    private final CombatSource source;

    private float percentHealth;

    @Inject
    public DwarfWarriorPassive(UUID entityId) {
        super(entityId, null);
        this.source = new SimpleCombatSource(NAME, entityId);
        this.percentHealth = 1;
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        percentHealth = event.getNewPercentHealth();
    }

    @Override
    public void onEntityPreDamageEvent(EntityPreDamageEvent event) {
        float modifierValue = -(0.25f * (1 - percentHealth));
        CombatValueModifier modifier =
                new SimpleCombatValueModifier(CombatValueModifierType.PERCENT, modifierValue, source);
        event.addModifier(modifier);
    }
}
