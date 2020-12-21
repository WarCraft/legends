/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.mage.elementalist

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect._
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Location, Sound, SoundCategory, WorldService}

class ChainLightning(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    target: Entity
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_BEE_DEATH

  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)

  chain(target)

  private def chain(entity: Entity, count: Int = 1): Unit = {
    entityService.damageEntity(entity.id, snapshot.damage)
    new ChainLightning.ParticleEffect(caster.location, target.location)
    worldService.playSound(caster.location, castSound, soundCategory)

    if (count < 3) {
      taskService.runLater(
        500.millis,
        () => {
          hitbox.collideEntities(entity.location, entity.location)
            .filter { _.isGrounded }
            .foreach { chain(_, count + 1) }
        }
      )
    }
  }

  override def onHit(entity: Entity): Boolean = false

  override def onMiss(entity: Entity): Boolean = false
}

object ChainLightning {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new ChainLightning(caster, snapshot, entityTarget.get)
  }

  class ParticleEffect(origin: Location, target: Location)(implicit
      particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends Effect {
    private final val particleCount = 20

    private val movement = Movement.oneOff(origin)

    override protected val location: () => Location = movement.get

    override def tick(): Unit = stop()

    {
      val particle = Particle.FireworksSpark
      val vectors = EffectVectors.line(target - origin, particleCount)
      val renderer = EffectRenderer(particle, vectors)
      addRenderer(renderer)

      start()
    }
  }
}
