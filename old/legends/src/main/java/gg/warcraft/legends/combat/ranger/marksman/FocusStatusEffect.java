package gg.warcraft.legends.combat.ranger.marksman;

import gg.warcraft.monolith.combat.event.MonolithAbilityHitEvent;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;

public class FocusStatusEffect extends AbstractStatusEffect {

    private final MonolithEntity entity;

    private double damageModifier;

    public FocusStatusEffect(MonolithEntity entity, double modifier) {
        this.entity = entity;
        this.damageModifier = modifier;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void onAbilityHitEvent(MonolithAbilityHitEvent event) {
        double damage = event.getAbility().getConfiguration().get(AbilityConfiguration.Attribute.DAMAGE);
        event.getAbility().getConfiguration().set(AbilityConfiguration.Attribute.DAMAGE, damage * damageModifier);
    }
}
