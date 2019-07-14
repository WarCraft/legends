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
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.location.Location;
import gg.warcraft.monolith.api.world.location.Orientation;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class HailBlast extends AbstractAbility implements RangedAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.BLOCK_SNOW_BREAK;
    private static final Sound CAST_SOUND_2 = Sound.ENTITY_SILVERFISH_HURT;

    private final EntityCommandService entityCommandService;

    @Inject
    public HailBlast(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                     WorldCommandService worldCommandService, TaskService taskService, TimeUtils timeUtils,
                     MovementFactory movementFactory, HitboxFactory hitboxFactory, MageEffectFactory mageEffectFactory,
                     @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                     @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;

        float range = snapshot.getFloat(AbilityConfigurationValue.RANGE);
        float speed = snapshot.getFloat(AbilityConfigurationValue.SPEED);
        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);

        Location origin = caster.getEyeLocation();
        Orientation orientation = caster.getEyeOrientation();
        Vector3f direction = orientation.toVector();
        Vector3f velocity = direction.mul(speed);
        Movement movement = movementFactory.createLinearMovement(origin, velocity, range);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());

        Cancellable effect = mageEffectFactory.createHailBlastEffect(movement, orientation.getPitch(), orientation.getYaw());
        abilityCommandService.createProjectile(this, movement, hitbox, effect::cancel);

        worldCommandService.playSound(origin, CAST_SOUND, SOUND_CATEGORY, 1, 1);
        worldCommandService.playSound(origin, CAST_SOUND_2, SOUND_CATEGORY, 1, 0.1f);
        for (int i = 0; i <= 12; i += 2) {
            Duration delay = timeUtils.createDurationInTicks(i);
            taskService.runLater(() -> worldCommandService.playSound(origin, CAST_SOUND, SOUND_CATEGORY, 1, 1), delay);
        }
    }

    @Override
    public boolean onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        PotionEffect slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.addPotionEffect(entity.getId(), slow);
        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }
}
