package gg.warcraft.legends.app.combat.mage.human;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.block.Block;

import javax.annotation.Nullable;

public class Barrier extends AbstractAbility implements RangedAbility {

    @Inject
    public Barrier(EntityCommandService entityCommandService, @Assisted AbilitySnapshot snapshot,
                   @Assisted("caster") Entity caster, @Assisted("target") @Nullable Entity rangedTarget,
                   @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        PotionEffect absorption = snapshot.getPotionEffect(PotionEffectType.ABSORPTION);
        entityCommandService.addPotionEffect(getCasterId(), absorption);

        // TODO play sound
    }

    @Override
    public boolean onHit(Entity entity) {
        // do nothing
        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        // do nothing
        return false;
    }
}
