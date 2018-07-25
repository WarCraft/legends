package gg.warcraft.legends.app.combat.mage.effect;

import com.google.inject.name.Named;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.world.Location;

import java.util.function.Supplier;

public interface MageEffectFactory {

    @Named("fireball")
    Cancellable createFireballEffect(Supplier<Location> location);
}
