package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.AttributeModifier;
import gg.warcraft.monolith.entity.SimpleMonolithEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public abstract class SimpleLegendsEntity extends SimpleMonolithEntity implements LegendsEntity {
    private static final double BASE_HEALTH = 40;

    public SimpleLegendsEntity() {
        super(LegendsAttribute.values());
    }

    protected void recalculateMaxHealth() {
        double extraHealth = getAttributeValue(LegendsAttribute.VITALITY);
        AttributeInstance healthAttribute = this.getAttributable().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        healthAttribute.setBaseValue(BASE_HEALTH + extraHealth);
    }

    @Override
    public void addAttributeModifier(AttributeModifier modifier) {
        super.addAttributeModifier(modifier);
        recalculateMaxHealth();
    }

    @Override
    public void removeAttributeModifier(AttributeModifier modifier) {
        super.removeAttributeModifier(modifier);
        recalculateMaxHealth();
    }
}
