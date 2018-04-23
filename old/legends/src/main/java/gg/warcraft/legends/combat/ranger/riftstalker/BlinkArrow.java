package gg.warcraft.legends.combat.ranger.riftstalker;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class BlinkArrow extends SimpleMonolithAbility {

    public BlinkArrow(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Blink Arrow";
    }
}
