package gg.warcraft.legends.app.combat.mage.wizard;

import gg.warcraft.combat.api.ability.RangedAbilityFactory;
import gg.warcraft.combat.api.ability.RangedAbilityFlag;

import java.util.EnumSet;

public interface TeleportFactory extends RangedAbilityFactory {

    @Override
    default EnumSet<RangedAbilityFlag> getFlags() {
        return EnumSet.of(RangedAbilityFlag.BLOCK_TARGET_REQUIRED);
    }
}
