package gg.warcraft.legends.combat.passive;

import gg.warcraft.monolith.combat.event.MonolithEntityHealthChangedEvent;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.attribute.Attribute;

public class OrcMagePassive extends AbstractStatusEffect {
    private static final long COOLDOWN = 40 * TimeUtils.MILLIS_PER_SECOND;

    private final MonolithEntity entity;

    private long cooldown;

    public OrcMagePassive(MonolithEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void onHealthChangedEvent(MonolithEntityHealthChangedEvent event) {
        if (System.currentTimeMillis() >= cooldown) {
            double maxHealth = event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (event.getEntity().getHealth() <= 0.75 * maxHealth) {
                cooldown = System.currentTimeMillis() + COOLDOWN;
                new OrcMagePassiveTotem(entity);
            }
        }
    }
}
