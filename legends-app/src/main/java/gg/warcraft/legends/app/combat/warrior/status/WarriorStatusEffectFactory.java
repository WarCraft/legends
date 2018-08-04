package gg.warcraft.legends.app.combat.warrior.status;

import com.google.inject.name.Named;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.util.Duration;

import java.util.UUID;

public interface WarriorStatusEffectFactory {

    @Named("grudge")
    StatusEffect createGrudgeStatusEffect(UUID entityId, UUID berzekerId, float heal, Duration duration);

    @Named("standYourGround")
    StatusEffect createStandYourGroundStatusEffect(UUID entityId, Duration duration);
}
