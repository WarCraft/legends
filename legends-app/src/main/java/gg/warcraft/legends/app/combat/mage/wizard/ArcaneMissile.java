package gg.warcraft.legends.app.combat.mage.wizard;

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
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import org.joml.Vector3fc;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ArcaneMissile extends AbstractAbility implements RangedAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_ENDERDRAGON_HURT;
    private static final Sound HIT_SOUND = Sound.ENTITY_ENDERDRAGON_HURT;

    private final EntityCommandService entityCommandService;
    private final WorldCommandService worldCommandService;

    @Inject
    public ArcaneMissile(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                         EntityQueryService entityQueryService,
                         WorldCommandService worldCommandService, MovementFactory movementFactory,
                         HitboxFactory hitboxFactory, MageEffectFactory mageEffectFactory,
                         @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                         @Assisted("target") Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.worldCommandService = worldCommandService;

        Location location = caster.getEyeLocation();
        Vector3f direction = caster.getEyeLocation().getRotation();
        Supplier<Location> targetSupplier = () -> {
            Entity target = entityQueryService.getEntity(rangedTarget.getId());
            return target.getEyeLocation();
        };
        float speed = snapshot.getFloat(AbilityConfigurationValue.SPEED);
        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);
        Vector3fc tempDir = new org.joml.Vector3f(direction.x(), direction.y(), direction.z());
        Movement movement = movementFactory.createHomingMovement(location, tempDir, speed, targetSupplier);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());

        Cancellable effect = mageEffectFactory.createArcaneMissileEffect(movement);
        abilityCommandService.createProjectile(this, movement, hitbox, effect::cancel);

        worldCommandService.playSound(location, CAST_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public boolean onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        entityCommandService.damage(entity.getId(), damage);
        worldCommandService.playSound(entity.getLocation(), HIT_SOUND, SOUND_CATEGORY, 1, 1);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        // do nothing
        return false;
    }
}
