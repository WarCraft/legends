package gg.warcraft.legends.combat.warrior.overlord;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class Boulder extends SimpleMonolithAbility {

    public Boulder(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Boulder";
    }
}
