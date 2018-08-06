package gg.warcraft.legends.app.combat.ranger.effect;

import com.google.inject.name.Named;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.util.Cancellable;

public interface RangerEffectFactory {

    @Named("focus")
    Cancellable createFocusEffect(Entity target);
}
