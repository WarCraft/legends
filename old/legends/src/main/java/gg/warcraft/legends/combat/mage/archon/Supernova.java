package gg.warcraft.legends.combat.mage.archon;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class Supernova extends SimpleMonolithAbility {

    public Supernova(MonolithEntity caster) {
        super(caster);
        new SupernovaProjectile(caster, 1); // TODO: get value from config
    }

    @Override
    public String toString() {
        return "Supernova";
    }
}
