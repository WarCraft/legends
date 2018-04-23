package gg.warcraft.legends.combat.passive;

import gg.warcraft.monolith.combat.event.MonolithAbilityHitEvent;
import gg.warcraft.monolith.combat.event.MonolithEntityHealthChangedEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;
import org.bukkit.attribute.Attribute;

public class OrcWarriorPassive extends AbstractStatusEffect {

    private final MonolithEntity entity;

    private double damageModifier;

    public OrcWarriorPassive(MonolithEntity entity) {
        this.entity = entity;
        this.damageModifier = 1;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void onHealthChangedEvent(MonolithEntityHealthChangedEvent event) {
        double health = event.getEntity().getHealth();
        double maxHealth = event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        damageModifier = health / maxHealth <= 0.5 ? 1.15 : 1;
    }

    @Override
    public void onAbilityHitEvent(MonolithAbilityHitEvent event) {
        double damage = event.getAbility().getConfiguration().get(AbilityConfiguration.Attribute.DAMAGE);
        event.getAbility().getConfiguration().set(AbilityConfiguration.Attribute.DAMAGE, damage * damageModifier);
    }
}
