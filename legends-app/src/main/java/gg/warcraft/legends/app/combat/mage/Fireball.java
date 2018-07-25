package gg.warcraft.legends.app.combat.mage;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Hitbox;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.SimpleAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationEffect;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.block.Block;
import org.joml.Vector3f;

public class Fireball extends SimpleAbility implements RangedAbility {
    private static final float MAX_DISTANCE = 30;

    private final EntityCommandService entityCommandService;

    @Inject
    public Fireball(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                    MovementFactory movementFactory, HitboxFactory hitboxFactory, @Assisted AbilitySnapshot snapshot,
                    @Assisted("caster") Entity caster, @Assisted("target") Entity rangedTarget,
                    @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;

        double radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);
        Location location = caster.getEyeLocation();
        float velocity = snapshot.getFloat(AbilityConfigurationValue.VELOCITY);
        Vector3f direction = caster.getEyeLocation().getDirection();
        Movement movement = movementFactory.createLinearMovement(location, direction, MAX_DISTANCE);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());
        abilityCommandService.createProjectile(this, movement, hitbox);

        // TODO create fireball effect
        // TODO play sound at cast location and fly locations (maybe use onTick for this?
    }

    @Override
    public boolean onHit(Entity entity) {
        float damage = getSnapshot().getFloat(AbilityConfigurationValue.DAMAGE);
        Duration burnDuration = getSnapshot().getDuration(AbilityConfigurationEffect.BURN);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.setFireTicks(entity.getId, burnDuration);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return true;
    }
}
