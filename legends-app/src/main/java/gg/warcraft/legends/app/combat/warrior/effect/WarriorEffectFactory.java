package gg.warcraft.legends.app.combat.warrior.effect;

import com.google.inject.name.Named;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.util.Duration;

public interface WarriorEffectFactory {

    @Named("grudge")
    void createGrudgeEffect(Entity target, Duration duration);

    @Named("hamstring")
    void createHamstringEffect(Entity target, Duration duration);

    @Named("standYourGround")
    void createStandYourGroundEffect(Entity target, Duration duration);
}
