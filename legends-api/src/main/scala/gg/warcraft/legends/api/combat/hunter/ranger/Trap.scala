/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter.ranger

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.{Block, BlockBisection, BlockFace, Trapdoor}
import gg.warcraft.monolith.api.block.spoofing.BlockSpoofingService
import gg.warcraft.monolith.api.block.variant.TrapdoorVariant
import gg.warcraft.monolith.api.core.Cancellable
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.world.{
  BlockLocation, Location, Sound, SoundCategory, WorldService
}

object Trap {
  class Factory(implicit
      taskService: TaskService,
      entityService: EntityService,
      playerService: PlayerService,
      worldService: WorldService,
      projectileService: ProjectileService,
      blockSpoofingService: BlockSpoofingService
  ) extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Trap(caster, snapshot)
  }
}

class Trap(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    taskService: TaskService,
    entityService: EntityService,
    playerService: PlayerService,
    worldService: WorldService,
    projectileService: ProjectileService,
    blockSpoofingService: BlockSpoofingService
) extends RangedAbility
    with Cancellable {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.BLOCK_IRON_TRAPDOOR_OPEN
  private final val hitSound = Sound.BLOCK_IRON_TRAPDOOR_CLOSE

  private var cancelled = false

  private val blockLocation: BlockLocation = caster.location
  private val currentBlock = worldService.getBlock(blockLocation)
  private val fakeBlock = Trapdoor(
    currentBlock.location,
    TrapdoorVariant.OAK,
    BlockFace.NORTH,
    BlockBisection.BOTTOM,
    powered = false,
    flooded = false,
    open = false
  )

  private val spoofedPlayerIds = playerService.getOnlinePlayers
    .filter { _.team == caster.team }
    .map { _.id }

  spoofedPlayerIds.foreach {
    blockSpoofingService.spoofBlock(_, fakeBlock)
  } // TODO add overload for same block multiple people

  private val trapLocation = BlockLocation.toLocation(blockLocation) + (.5f, 0, .5f)
  private val movement = Movement.stationary(trapLocation)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  private val projectileId =
    projectileService.createProjectile(this, movement, hitbox)

  taskService.runLater(snapshot.duration, cancel)
  worldService.playSound(caster.location, castSound, soundCategory)

  override def cancel(): Unit = if (!cancelled) {
    cancelled = true
    projectileService.removeProjectile(projectileId)
    spoofedPlayerIds.foreach { blockSpoofingService.unspoofBlock(_, trapLocation) }
  }

  override def onHit(entity: Entity): Boolean = {
    val startLocation = entity.location
    entityService.addPotionEffect(entity.id, snapshot.poison)
    entityService.freezeEntity(entity.id, snapshot.root)

    // prevent yeeting into space
    taskService.runNextTick(() => {
      entityService.teleportEntity(entity.id, startLocation)
      entityService.setVelocity(entity.id, Vector3f())
    })

    worldService.playSound(entity.location, hitSound, soundCategory)

    cancel()
    true
  }

  override def onMiss(entity: Entity): Boolean = {
    cancel()
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onGrounded(location: Location): Boolean = false
}
