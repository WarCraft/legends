package gg.warcraft.legends.app.combat.ranger.hunter;

import gg.warcraft.combat.api.ability.MeleeAbilityFactory;
import gg.warcraft.combat.api.ability.MeleeAbilityFlag;

import java.util.EnumSet;

public interface DisengageFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}
