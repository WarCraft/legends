package gg.warcraft.legends.app.combat.mage.wizard;

import gg.warcraft.combat.api.ability.RangedAbilityFactory;
import gg.warcraft.combat.api.ability.RangedAbilityFlag;

import java.util.EnumSet;

public interface MassBamboozleFactory extends RangedAbilityFactory {

    @Override
    default EnumSet<RangedAbilityFlag> getFlags() {
        return EnumSet.of(RangedAbilityFlag.ENTITY_OR_BLOCK_TARGET_REQUIRED);
    }
}
