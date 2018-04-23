package gg.warcraft.legends.combat.warrior.harbinger;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class GraspOfTheDead extends SimpleMonolithAbility {

    public GraspOfTheDead(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Grasp of The Dead";
    }
}
