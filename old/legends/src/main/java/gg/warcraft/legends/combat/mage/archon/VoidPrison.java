package gg.warcraft.legends.combat.mage.archon;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class VoidPrison extends SimpleMonolithAbility {

    public VoidPrison(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Void Prison";
    }
}
