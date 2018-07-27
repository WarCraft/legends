package gg.warcraft.legends.app.combat.mage.wizard;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.BlockTypeUtils;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import gg.warcraft.monolith.api.world.service.WorldQueryService;

public class MassBamboozle extends AbstractAbility implements RangedAbility {

    @Inject
    public MassBamboozle(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                         WorldQueryService worldQueryService, WorldCommandService worldCommandService,
                         MovementFactory movementFactory, HitboxFactory hitboxFactory, BlockTypeUtils blockTypeUtils,
                         @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                         @Assisted("target") Entity rangedTarget, @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);

        // TODO do the thing
    }

    @Override
    public boolean onHit(Entity entity) {
        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }
}
