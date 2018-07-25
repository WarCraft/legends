package gg.warcraft.legends.app.combat.warrior.berserker;

import gg.warcraft.combat.api.ability.MeleeAbilityFactory;
import gg.warcraft.combat.api.ability.MeleeAbilityFlag;

import java.util.EnumSet;

public interface ShatterFactory extends MeleeAbilityFactory {

    @Override
    default EnumSet<MeleeAbilityFlag> getFlags() {
        return EnumSet.noneOf(MeleeAbilityFlag.class);
    }
}
