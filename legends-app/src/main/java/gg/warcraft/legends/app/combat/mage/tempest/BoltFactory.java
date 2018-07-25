package gg.warcraft.legends.app.combat.mage.tempest;

import gg.warcraft.combat.api.ability.RangedAbilityFactory;
import gg.warcraft.combat.api.ability.RangedAbilityFlag;

import java.util.EnumSet;

public interface BoltFactory extends RangedAbilityFactory {

    @Override
    default EnumSet<RangedAbilityFlag> getFlags() {
        return EnumSet.of(RangedAbilityFlag.ENTITY_OR_BLOCK_TARGET_REQUIRED);
    }
}
