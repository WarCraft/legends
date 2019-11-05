package gg.warcraft.legends.app.combat.mage.tempest;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Hitbox;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.BlockLocation;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.WorldService;

import javax.annotation.Nullable;

public class Bolt extends AbstractAbility implements RangedAbility {
    private final EntityCommandService entityCommandService;

    @Inject
    public Bolt(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                WorldService WorldService, HitboxFactory hitboxFactory, MovementFactory movementFactory,
                @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;

        Location location;
        if (rangedTarget != null) {
            location = rangedTarget.getLocation();
        } else if (blockTarget != null) {
            BlockLocation blockLocation = blockTarget.location();
            location = blockLocation.toLocation().add(0.5f, 1f, 0.5f);
        } else {
            throw new IllegalArgumentException("Failed to cast Bolt with null entity and block targets.");
        }

        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);
        Movement movement = movementFactory.createOneOffMovement(location);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());
        abilityCommandService.createProjectile(this, movement, hitbox, () -> {});

        WorldService.strikeLightning(location, true);
    }

    @Override
    public boolean onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        entityCommandService.damage(entity.getId(), damage);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return true;
    }
}
