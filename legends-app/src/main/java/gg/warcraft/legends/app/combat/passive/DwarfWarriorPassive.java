package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.event.EntityHealthChangedEvent;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.app.entity.status.AbstractStatusEffect;

import java.util.UUID;

public class DwarfWarriorPassive extends AbstractStatusEffect implements StatusEffect {
    private double damageModifier;

    @Inject
    public DwarfWarriorPassive(UUID entityId) {
        super(entityId, null);
        this.damageModifier = 1;
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        // TODO impl, max health on event would be nice, otherwise on Entity
        float health = 0; // event.getEntity().getHealth();
        float maxHealth = 0; // event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        damageModifier = 1 - (0.25 * (1 - health / maxHealth));
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        // TODO impl
        // float damage = event.getDamage();
        // event.setDamage(event.getDamage() * damageModifier);
    }
}
