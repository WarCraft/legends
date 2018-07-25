package gg.warcraft.legends.app.combat.passive;

import gg.warcraft.monolith.combat.event.MonolithEntityHealthChangedEvent;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.potion.PotionEffectType;

public class HumanMagePassive extends AbstractStatusEffect {
    private static final int DURATION = 5 * TimeUtils.TICKS_PER_SECOND;
    private static final int ABSORPTION = 2;
    private static final long COOLDOWN = 300L * TimeUtils.MILLIS_PER_SECOND;

    private final MonolithEntity entity;
    private final PotionConfiguration absorption;

    private long cooldown;

    public HumanMagePassive(MonolithEntity entity) {
        this.entity = entity;
        this.absorption = new PotionConfiguration(new Duration(DURATION, Duration.Type.TICKS), ABSORPTION);
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void onHealthChangedEvent(MonolithEntityHealthChangedEvent event) {
        if (System.currentTimeMillis() > cooldown) {
            double maxHealth = event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (event.getEntity().getHealth() <= 0.5 * maxHealth) {
                cooldown = System.currentTimeMillis() + COOLDOWN;
                event.getEntity().addPotionEffect(PotionEffectType.ABSORPTION, absorption);
            }
        }
    }
}
