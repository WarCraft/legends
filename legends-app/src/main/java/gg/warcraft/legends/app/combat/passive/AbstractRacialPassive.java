package gg.warcraft.legends.app.combat.passive;

import gg.warcraft.monolith.api.entity.status.AbstractStatusEffect;
import gg.warcraft.monolith.api.util.Duration;

import java.util.UUID;

public abstract class AbstractRacialPassive extends AbstractStatusEffect {
    private static final String TYPE = "legends.racial";

    public AbstractRacialPassive(UUID entityId, String name, Duration duration) {
        super(entityId, name, TYPE, duration);
    }
}
