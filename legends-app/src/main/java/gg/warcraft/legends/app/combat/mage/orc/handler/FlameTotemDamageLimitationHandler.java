package gg.warcraft.legends.app.combat.mage.orc.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.combat.api.ability.event.AbilityPreCastEvent;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.CombatSource;
import gg.warcraft.monolith.api.combat.value.CombatValueModifier;
import gg.warcraft.monolith.api.combat.value.CombatValueModifierType;

import java.util.UUID;

public class FlameTotemDamageLimitationHandler implements CombatSource {
    private static final String FLAME_TOTEM = "Flame Totem";

    private final CombatValueModifier modifier;

    public FlameTotemDamageLimitationHandler(CombatFactory combatFactory) {
        this.modifier = combatFactory.createCombatValueModifier(
                CombatValueModifierType.OVERRIDE, 2f, this);
    }

    @Override
    public String getName() {
        return FLAME_TOTEM;
    }

    @Override
    public UUID getEntityId() {
        return null;
    }

    @Subscribe
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        if ("Flame Totem".equals(event.getAbilityName())) {
            event.addDamageModifier(modifier);
        }
    }
}
