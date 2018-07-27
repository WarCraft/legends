package gg.warcraft.legends.app.combat.mage.effect;

import com.google.inject.name.Named;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Location;

public interface MageEffectFactory {

    @Named("arcaneMissile")
    Cancellable createArcaneMissileEffect(Movement movement);

    @Named("fireball")
    Cancellable createFireballEffect(Movement movement);

    @Named("moltenStormRing")
    Cancellable createMoltenStormRingEffect(Location location, Duration duration);

    @Named("flameTotemFireball")
    Cancellable createFlameTotemFireballEffect(Movement movement);
}
