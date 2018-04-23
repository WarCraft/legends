package gg.warcraft.legends.combat.warrior.overlord;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class Rally extends SimpleMonolithAbility {

    public Rally(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Rally";
    }
}
