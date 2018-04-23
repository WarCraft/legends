package gg.warcraft.legends.combat.mage.oracle;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.MonolithEntity;

public class AstralPulse extends SimpleMonolithAbility {

    public AstralPulse(MonolithEntity caster) {
        super(caster);
        caster.sendMessage(this + " fired, but is missing implementation!");
    }

    @Override
    public String toString() {
        return "Astral Pulse";
    }
}
