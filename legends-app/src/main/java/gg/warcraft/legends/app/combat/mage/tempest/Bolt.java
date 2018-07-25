package gg.warcraft.legends.app.combat.mage.tempest;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.world.BlockLocation;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import gg.warcraft.monolith.api.world.service.WorldQueryService;

import java.util.List;
import java.util.stream.Collectors;

public class Bolt extends AbstractAbility implements RangedAbility {

    @Inject
    public Bolt(EntityQueryService entityQueryService, EntityCommandService entityCommandService,
                WorldQueryService worldQueryService, WorldCommandService worldCommandService, AbilitySnapshot snapshot,
                @Assisted("caster") Entity caster, @Assisted("target") Entity rangedTarget, Block blockTarget) {
        super(caster.getId(), snapshot);
        Location location;
        if (rangedTarget != null) {
            location = rangedTarget.getLocation();
        } else if (blockTarget != null) {
            BlockLocation blockLocation = blockTarget.getLocation();
            location = worldQueryService.getLocation(blockLocation.getWorld().getType(),
                    blockLocation.getX() + 0.5f,
                    blockLocation.getY(),
                    blockLocation.getZ() + 0.5f);
        } else {
            throw new IllegalArgumentException("Failed to cast Bolt with null entity and block targets.");
        }

        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);
        // TODO worldCommandService.strikeLightning(location);    make distinction between effect and actual strike
        List<Entity> casualties = entityQueryService.getNearbyEntities(location, radius);
        casualties = casualties.stream()
                .filter(new HostileTo(caster))
                .collect(Collectors.toList());
        publishHitAndDamageEvents(this, casualties).forEach(damageEvent -> { // TODO abstract hit and damage events logic in command service so projectile handler can call it as well as abilities that need to?
            entityCommandService.damage(entity.getId(), newSnapshot.getDamage());
        });
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
