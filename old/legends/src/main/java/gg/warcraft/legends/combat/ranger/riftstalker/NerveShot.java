package gg.warcraft.legends.combat.ranger.riftstalker;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class NerveShot extends SimpleMonolithAbility {

    public NerveShot(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Nerve Shot";
    }
}
