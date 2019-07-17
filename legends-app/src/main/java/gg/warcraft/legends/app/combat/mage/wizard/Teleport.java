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
import gg.warcraft.monolith.api.world.block.BlockTypeUtils;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import gg.warcraft.monolith.api.world.service.WorldQueryService;

import javax.annotation.Nullable;

public class Teleport extends AbstractAbility implements RangedAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_ENDERMEN_TELEPORT;
    private static final Sound HIT_SOUND = Sound.ENTITY_ENDERMEN_TELEPORT;

    private final EntityCommandService entityCommandService;
    private final CombatValue damage;
    private final PotionEffect slow;

    @Inject
    public Teleport(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                    WorldQueryService worldQueryService, WorldCommandService worldCommandService,
                    MovementFactory movementFactory, HitboxFactory hitboxFactory, BlockTypeUtils blockTypeUtils,
                    DirectionUtils directionUtils,
                    @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                    @Assisted("target") @Nullable Entity rangedTarget, @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        this.slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);

        Location originalLocation = caster.getLocation();
        Location teleportLocation;
        Float teleportOrientation;

        BlockLocation targetLocation = blockTarget.getLocation();
        BlockLocation relativeLocation = targetLocation.add(0, 1, 0);
        Block relative = worldQueryService.getBlockAt(relativeLocation);
        if (blockTypeUtils.getNonSolids().contains(relative.getType())) {
            teleportLocation = new Location(
                    relativeLocation.getWorld(),
                    relativeLocation.getX(),
                    relativeLocation.getY(),
                    relativeLocation.getZ()
            ).add(0.5f, 0, 0.5f);
            teleportOrientation = caster.getEyeLocation().getYaw();
        } else {
            Vector3f targetToCaster = targetLocation.toLocation().sub(originalLocation).getTranslation();
            targetToCaster.normalize();
            BlockLocation adjustedLocation = targetLocation.toLocation()
                    .add(0.5f, 0.5f, 0.5f)
                    .add(targetToCaster)
                    .toBlockLocation();
            teleportLocation = new Location(
                    adjustedLocation.getWorld(),
                    adjustedLocation.getX(),
                    adjustedLocation.getY(),
                    adjustedLocation.getZ()
            ).add(0.5f, 1, 0.5f);
            teleportOrientation = caster.getEyeLocation().getYaw();
        }

        entityCommandService.teleport(caster.getId(), teleportLocation, directionUtils.toDirection(teleportOrientation));
        worldCommandService.playSound(originalLocation, CAST_SOUND, SOUND_CATEGORY, 1, 1);

        float radius = snapshot.getFloat(AbilityConfigurationValue.RADIUS);
        Movement movement = movementFactory.createOneOffMovement(teleportLocation);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());
        abilityCommandService.createProjectile(this, movement, hitbox, () -> {
        });
        worldCommandService.playSound(teleportLocation, HIT_SOUND, SOUND_CATEGORY, 1, 1);
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
