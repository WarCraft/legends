package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.CombatSource;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;
import gg.warcraft.monolith.api.entity.event.EntityPreDamageEvent;

import java.util.UUID;

public class DwarfWarriorPassive extends AbstractRacialPassive implements CombatSource {
    private static final String NAME = "Sturdiness";

    private final CombatFactory combatFactory;

    private float percentHealth;

    @Inject
    public DwarfWarriorPassive(CombatFactory combatFactory, @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.combatFactory = combatFactory;
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
                combatFactory.createCombatValueModifier(CombatValueModifierType.PERCENT, modifierValue, this);
        event.addModifier(modifier);
    }
}
