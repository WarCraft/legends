package gg.warcraft.legends.app.combat.mage;

import gg.warcraft.combat.api.ability.RangedAbilityFactory;
import gg.warcraft.combat.api.ability.RangedAbilityFlag;

import java.util.EnumSet;

public interface FireballFactory extends RangedAbilityFactory {

    @Override
    default EnumSet<RangedAbilityFlag> getFlags() {
        return EnumSet.noneOf(RangedAbilityFlag.class);
    }
}
