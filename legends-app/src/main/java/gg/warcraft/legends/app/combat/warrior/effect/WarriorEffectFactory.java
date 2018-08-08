package gg.warcraft.legends.app.combat.warrior.effect;

import com.google.inject.name.Named;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;

public interface WarriorEffectFactory {

    @Named("grudge")
    Cancellable createGrudgeEffect(Entity target, Duration duration);

    @Named("hamstring")
    Cancellable createHamstringEffect(Entity target, Duration duration);

    @Named("standYourGround")
    Cancellable createStandYourGroundEffect(Entity target, Duration duration);
}
