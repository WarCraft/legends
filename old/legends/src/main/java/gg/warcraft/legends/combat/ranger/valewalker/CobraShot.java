package gg.warcraft.legends.combat.ranger.valewalker;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class CobraShot extends SimpleMonolithAbility {

    public CobraShot(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Cobra Shot";
    }
}
