package gg.warcraft.legends.app.combat.passive;

import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.app.entity.status.AbstractStatusEffect;

import java.util.UUID;

public abstract class AbstractRacialPassive extends AbstractStatusEffect {

    public AbstractRacialPassive(UUID entityId, Duration duration) {
        super(entityId, duration);
    }

    @Override
    public String getType() {
        return "legends.racial";
    }
}
