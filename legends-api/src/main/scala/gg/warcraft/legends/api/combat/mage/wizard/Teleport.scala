package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.ParticleAdapter
import gg.warcraft.monolith.api.entity.{ Entity, EntityService }
import gg.warcraft.monolith.api.world.{ Sound, SoundCategory, WorldService }

object Teleport {
  class Factory(
      implicit taskService: TaskService,
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.BLOCK_TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Teleport(caster, snapshot)
  }
}

class Teleport(
    override val caster  : Entity               ,
override val snapshot: AbilitySnapshot
) (
    implicit entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
)extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDERMAN_TELEPORT
  private final val hitSound = Sound.ENTITY_ENDERMAN_TELEPORT

  // TODO

  worldService playSound (caster.location, castSound, soundCategory, 1, 1)

  override def onHit(entity: Entity): Boolean = {
    entityService damageEntity (entity.id, snapshot.damage)
    entityService addPotionEffect (entity.id, snapshot.slow)
    worldService playSound (entity.location, hitSound, soundCategory, 1, 1)
    false
  }

  override def onMiss(entity: Entity): Boolean = false
}

/*
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
    abilityCommandService.createProjectile(this, movement, hitbox, () -> {});
 */
