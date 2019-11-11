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
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.world.BlockLocation;
import gg.warcraft.monolith.api.world.DirectionUtils;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.WorldService;

import javax.annotation.Nullable;

public class Teleport extends AbstractAbility implements RangedAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_ENDERMAN_TELEPORT;
    private static final Sound HIT_SOUND = Sound.ENTITY_ENDERMAN_TELEPORT;

    private final EntityCommandService entityCommandService;
    private final CombatValue damage;
    private final PotionEffect slow;

    @Inject
    public Teleport(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                    WorldService WorldService, MovementFactory movementFactory, HitboxFactory hitboxFactory,
                    DirectionUtils directionUtils,
                    @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                    @Assisted("target") @Nullable Entity rangedTarget, @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        this.slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);

        Location originalLocation = caster.getLocation();
        Location teleportLocation;
        float teleportOrientation;

        BlockLocation targetLocation = blockTarget.location();
        BlockLocation relativeLocation = targetLocation.add(0, 1, 0);
        Block relative = WorldService.getBlock(relativeLocation);
        if (!relative.solid()) {
            teleportLocation = new Location(
                    relativeLocation.world(),
                    relativeLocation.x(),
                    relativeLocation.y(),
                    relativeLocation.z()
            ).add(0.5f, 0, 0.5f);
            teleportOrientation = caster.getEyeLocation().yaw();
        } else {
            Vector3f targetToCaster = targetLocation.toLocation().subtract(originalLocation).translation();
            targetToCaster.normalize();
            BlockLocation adjustedLocation = targetLocation.toLocation()
                    .add(0.5f, 0.5f, 0.5f)
                    .add(targetToCaster)
                    .toBlockLocation();
            teleportLocation = new Location(
                    adjustedLocation.world(),
                    adjustedLocation.x(),
                    adjustedLocation.y(),
                    adjustedLocation.z()
            ).add(0.5f, 1, 0.5f);
            teleportOrientation = caster.getEyeLocation().yaw();
        }

        entityCommandService.teleport(caster.getId(), teleportLocation, directionUtils.toDirection(teleportOrientation));
        WorldService.playSound(originalLocation, CAST_SOUND, SOUND_CATEGORY, 1, 1);

        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);
        Movement movement = movementFactory.createOneOffMovement(teleportLocation);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());
        abilityCommandService.createProjectile(this, movement, hitbox, () -> {
        });
        WorldService.playSound(teleportLocation, HIT_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public boolean onHit(Entity entity) {
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.addPotionEffect(entity.getId(), slow);
        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        // do nothing
        return false;
    }
}
