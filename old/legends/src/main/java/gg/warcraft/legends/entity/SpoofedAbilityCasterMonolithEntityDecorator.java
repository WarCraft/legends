package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.Attribute;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.entity.SimpleMonolithEntity;
import org.bukkit.entity.LivingEntity;

public class SpoofedAbilityCasterMonolithEntityDecorator extends SimpleMonolithEntity {

    private final MonolithEntity caster;

    public SpoofedAbilityCasterMonolithEntityDecorator(MonolithEntity caster) {
        super(new Attribute[0]);
        this.caster = caster;
    }

    @Override
    public Race getRace() {
        return caster.getRace();
    }

    @Override
    public double getAttributeValue(Attribute attribute) {
        return 0;
    }

    @Override
    public LivingEntity getLivingEntity() {
        return ((SimpleMonolithEntity) caster).getLivingEntity();
    }
}
