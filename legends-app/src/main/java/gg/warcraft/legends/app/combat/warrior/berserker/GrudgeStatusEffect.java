package gg.warcraft.legends.app.combat.warrior.berserker;


import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.util.Duration;

import java.util.UUID;

public class GrudgeStatusEffect implements StatusEffect {
    private final UUID entityId;
    private final UUID casterId;
    private final double heal;

    public GrudgeStatusEffect(UUID entityId, UUID casterId, double heal) {
        this.entityId = entityId;
        this.casterId = casterId;
        this.heal = heal;
    }

    @Override
    public UUID getEntityId() {
        return entityId;
    }

    @Override
    public Duration getDuration() {
        return null;
    }

    @Override
    public boolean hasExpired() {
        return false; // TODO rethink expired func, could check everytime they are iterated and remove all expired
    }

    @Override
    public void onDamageEvent(MonolithEntityDamageEvent event) {
        if (event.getDamager() != null) {
            double maxHealth = berserker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (berserker.getHealth() + heal <= maxHealth) {
                berserker.sendMessage("[Grudge] Healing you to " + (berserker.getHealth() + heal));
                berserker.setHealth(berserker.getHealth() + heal);
            } else {
                berserker.sendMessage("[Grudge] Healing you to full");
                berserker.setHealth(maxHealth);
            }
        }
    }
}
