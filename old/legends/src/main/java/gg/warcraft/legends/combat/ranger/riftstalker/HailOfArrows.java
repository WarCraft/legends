package gg.warcraft.legends.combat.ranger.riftstalker;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class HailOfArrows extends SimpleMonolithAbility {

    public HailOfArrows(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Hail of Arrows";
    }
}
