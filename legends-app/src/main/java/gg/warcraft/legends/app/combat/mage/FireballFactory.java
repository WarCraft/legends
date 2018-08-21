package gg.warcraft.legends.app.combat.mage;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.RangedAbilityFactory;
import gg.warcraft.combat.api.ability.RangedAbilityFlag;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.world.block.Block;

import java.util.EnumSet;

public interface FireballFactory extends RangedAbilityFactory {

    @Override
    default EnumSet<RangedAbilityFlag> getFlags() {
        return EnumSet.noneOf(RangedAbilityFlag.class);
    }

    @Override
    @Named("Fireball")
    RangedAbility create(AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                         @Assisted("target") Entity rangedTarget, Block blockTarget);
}
