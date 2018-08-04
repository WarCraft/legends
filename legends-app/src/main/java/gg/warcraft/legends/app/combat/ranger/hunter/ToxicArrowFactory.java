package gg.warcraft.legends.app.combat.ranger.hunter;

import gg.warcraft.combat.api.ability.MeleeAbilityFactory;
import gg.warcraft.combat.api.ability.MeleeAbilityFlag;

import java.util.EnumSet;

public interface ToxicArrowFactory extends MeleeAbilityFactory { // TODO rename this AbilityFactory and the other one to TargetedAbilityFactory? Who then gets to register for projectiles?

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}
