package gg.warcraft.legends.app.combat.mage.effect;

import com.google.inject.name.Named;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.monolith.api.util.Cancellable;

public interface MageEffectFactory {

    @Named("fireball")
    Cancellable createFireballEffect(Movement movement);
}
