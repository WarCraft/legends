package gg.warcraft.legends.app.combat.warrior.status;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.util.Duration;

import java.util.UUID;

public interface WarriorStatusEffectFactory {

    @Named("grudge")
    StatusEffect createGrudgeStatusEffect(@Assisted("entity") UUID entityId, @Assisted("caster") UUID berzekerId,
                                          Duration duration, CombatValue heal);

    @Named("standYourGround")
    StatusEffect createStandYourGroundStatusEffect(UUID entityId, Duration duration);
}
