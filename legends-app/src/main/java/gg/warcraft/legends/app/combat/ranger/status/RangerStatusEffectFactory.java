package gg.warcraft.legends.app.combat.ranger.status;

import com.google.inject.name.Named;
import gg.warcraft.monolith.api.entity.status.StatusEffect;

import java.util.UUID;

public interface RangerStatusEffectFactory {

    @Named("focus")
    StatusEffect createFocusStatusEffect(UUID entityId, float damageModifier);
}
