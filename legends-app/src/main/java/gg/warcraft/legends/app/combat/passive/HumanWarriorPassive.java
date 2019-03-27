package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.entity.event.EntityPreFatalDamageEvent;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.UUID;

public class HumanWarriorPassive extends AbstractRacialPassive {
    private static final String NAME = "Last Stand";
    private static final int DURATION_IN_MILLIS = 4000;
    private static final long COOLDOWN_IN_MILLIS = 300 * TimeUtils.MILLIS_PER_SECOND;

    private long invulnerableUntil;
    private long cooldown;

    @Inject
    public HumanWarriorPassive(@Assisted UUID entityId) {
        super(entityId, NAME, null);
    }

    @Override
    public void onEntityPreFatalDamageEvent(EntityPreFatalDamageEvent event) {
        long now = System.currentTimeMillis();
        if (now <= invulnerableUntil) {
            event.cancel();
        } else if (now >= cooldown) {
            invulnerableUntil = now + DURATION_IN_MILLIS;
            cooldown = now + COOLDOWN_IN_MILLIS;
            event.cancel();
        }
    }
}
