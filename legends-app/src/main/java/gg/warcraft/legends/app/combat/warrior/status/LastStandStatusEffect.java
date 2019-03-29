package gg.warcraft.legends.app.combat.warrior.status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.entity.event.EntityPreFatalDamageEvent;
import gg.warcraft.monolith.api.entity.status.AbstractStatusEffect;
import gg.warcraft.monolith.api.util.Duration;

import java.util.UUID;

public class LastStandStatusEffect extends AbstractStatusEffect {
    private static final String NAME = "Last Stand";
    private static final String TYPE = "legends.armsmaster.laststand";

    @Inject
    public LastStandStatusEffect(@Assisted UUID entityId,
                                 @Assisted Duration duration) {
        super(entityId, NAME, TYPE, duration);
    }

    @Override
    public void onEntityPreFatalDamageEvent(EntityPreFatalDamageEvent event) {
        event.cancel();
    }
}
