package gg.warcraft.legends.api.combat.mage.wizard

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.combat.api.{ Hitbox, Movement }
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.{ Entity, EntityService }
import gg.warcraft.monolith.api.world.{ Sound, SoundCategory, WorldService }

object Teleport {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService
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
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_ENDERMAN_TELEPORT
  private final val hitSound = Sound.ENTITY_ENDERMAN_TELEPORT

  private val origin = caster.location
  private val rotation = caster.eyeLocation.rotation


  /* TODO impl relative block utils
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
   */

  entityService.teleportEntity(caster.id, target, rotation)
  worldService.playSound(origin, castSound, soundCategory)

  private val movement = Movement.oneOff(target)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  projectileService.createProjectile(this, movement, hitbox)

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.addPotionEffect(entity.id, snapshot.slow)
    worldService.playSound(entity.location, hitSound, soundCategory)
    false
  }

  override def onMiss(entity: Entity): Boolean = false
}
