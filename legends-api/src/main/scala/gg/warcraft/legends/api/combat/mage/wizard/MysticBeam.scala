package gg.warcraft.legends.api.combat.mage.wizard

import java.util.UUID

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.{Projectile, ProjectileService}
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.combat.CombatValue
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.effect.{Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

import scala.collection.mutable.ListBuffer

class MysticBeam(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_SILVERFISH_HURT
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  private val range = snapshot.range.modified
  private val velocity = snapshot.velocity.modified
  private val period = snapshot.duration.seconds.toInt.millis
  private val task = taskService.runTask(period, createProjectile)
  taskService.runLater(snapshot.duration, task.cancel)

  private def createProjectile(): Unit = {
    val movement = Movement.linear(
      caster.location + Vector3f.Offset.chest,
      caster.location.rotation * velocity,
      range
    )
    val effect = new MysticBeam.ParticleEffect(movement)
    projectileService.createProjectile(this, movement, hitbox, effect.cancel)
  }

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    worldService.playSound(caster.location, castSound, soundCategory)
    false
  }

  override def onMiss(entity: Entity): Boolean = false

}

object MysticBeam {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new MysticBeam(caster, snapshot)
  }

  class ParticleEffect(
      movement: Movement
  )(implicit
      particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends Effect(movement.get) {
    private val particle = Particle.Redstone.color(Particle.Color.Purple)
    private val vectors = EffectVectors.origin()
    private val renderer = EffectRenderer(particle, vectors)
    addRenderer(renderer)
  }
}
