package gg.warcraft.legends.app.combat.mage;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Hitbox;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationEffect;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import javax.annotation.Nullable;

public class Fireball extends AbstractAbility implements RangedAbility {
    private static final float MAX_DISTANCE = 30;
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_GHAST_SHOOT;

    private final EntityCommandService entityCommandService;

    @Inject
    public Fireball(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                    WorldCommandService worldCommandService, MovementFactory movementFactory,
                    HitboxFactory hitboxFactory, MageEffectFactory mageEffectFactory,
                    @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                    @Assisted("target") @Nullable Entity rangedTarget, @Nullable @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;

        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);
        Location location = caster.getEyeLocation();
        float speed = snapshot.getFloat(AbilityConfigurationValue.SPEED);
        Vector3f direction = caster.getEyeLocation().getRotation();
        Vector3f velocity = direction.mul(speed);
        Movement movement = movementFactory.createLinearMovement(location, velocity, MAX_DISTANCE);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());

        Cancellable effect = mageEffectFactory.createFireballEffect(movement);
        abilityCommandService.createProjectile(this, movement, hitbox, effect::cancel);

        worldCommandService.playSound(location, CAST_SOUND, SOUND_CATEGORY, 1, 1);
        // TODO play sound at fly locations (maybe use onTick for this or create something for sound similar to effect)
    }

    @Override
    public boolean onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        Duration burnDuration = getSnapshot().getDuration(AbilityConfigurationEffect.BURN);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.burn(entity.getId(), burnDuration);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }
}
