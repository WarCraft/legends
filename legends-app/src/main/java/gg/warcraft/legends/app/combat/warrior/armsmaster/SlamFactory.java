package gg.warcraft.legends.app.combat.warrior.armsmaster;

import gg.warcraft.combat.api.ability.MeleeAbilityFactory;
import gg.warcraft.combat.api.ability.MeleeAbilityFlag;

import java.util.EnumSet;

public interface SlamFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.of(MeleeAbilityFlag.TARGET_REQUIRED);
    }
}
