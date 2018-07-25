package gg.warcraft.legends.app.combat.warrior.berserker;

import gg.warcraft.combat.api.ability.MeleeAbilityFactory;
import gg.warcraft.combat.api.ability.MeleeAbilityFlag;

import java.util.EnumSet;

public interface GrudgeFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.of(MeleeAbilityFlag.TARGET_REQUIRED);
    }
}
