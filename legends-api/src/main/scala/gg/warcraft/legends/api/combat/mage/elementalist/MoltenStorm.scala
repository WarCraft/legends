package gg.warcraft.legends.api.combat.mage.elementalist

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.legends.api.combat.mage.Fireball
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect._
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.world.{Location, Sound, SoundCategory, WorldService}

import scala.util.Random

class MoltenStorm(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    blockTarget: Option[Block],
    entityTarget: Option[Entity]
)(implicit
    taskService: TaskService,
    entityService: EntityService,
    worldService: WorldService,
    projectileService: ProjectileService,
    particleAdapter: ParticleAdapter
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_GHAST_SHOOT
  private final val hitSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE
  private final val height = Vector3f(0, 20, 0)
  private final val offset = Vector3f(5, 0, 5)

  private val location =
    if (entityTarget.isDefined) entityTarget.get.location
    else blockTarget.get.location.toLocation + (.5f, 1f, .5f)
  private val origin = location + height + offset
  private val direction = (location - origin).normalized
  private val velocity = direction * snapshot.velocity.modified
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)

  private val spawnVectors = EffectVectors.ring(
    snapshot.combatValue("spawnRadius").modified,
    snapshot.count.modified.toInt
  )
  private val spawnLocations = Random.shuffle(spawnVectors.toList).iterator
    .map { _ * Random.nextFloat }
    .map { origin + _ }

  // setup tasks
  private val fireballPeriod =
    (snapshot.duration.millis / snapshot.count.modified).toInt.ticks
  private val task = taskService.runTask(fireballPeriod, spawnFireball)
  taskService.runLater(snapshot.duration, task.cancel)

  new MoltenStorm.ParticleEffect(location, snapshot.duration)
  worldService.playSound(caster.location, castSound, soundCategory)

  private def spawnFireball(): Unit = {
    val movement = Movement.linear(spawnLocations.next, velocity, snapshot.range.modified)
    val effect = new Fireball.ParticleEffect(movement)
    projectileService.createProjectile(this, movement, hitbox, effect.cancel)
    worldService.playSound(caster.location, castSound, soundCategory)
  }

  override def onHit(entity: Entity): Boolean = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.burnEntity(entity.id, snapshot.burn)
    worldService.playSound(entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false

  override def onGrounded(location: Location): Boolean = {
    worldService.playSound(location, hitSound, soundCategory)
    true
  }
}

object MoltenStorm {
  class Factory(implicit
      taskService: TaskService,
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService,
      particleAdapter: ParticleAdapter
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new MoltenStorm(caster, snapshot, blockTarget, entityTarget)
  }

  class ParticleEffect(
      location: Location,
      duration: Duration
  )(implicit
      taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect(() => location + Vector3f.Offset.feet) {
    private final val radius = 7.5f
    private final val particleCount = 100

    private final val particle = Particle.Redstone.color(Particle.Color.Orange)
    private final val vectors = EffectVectors.ring(radius, particleCount)
    private final val renderer = EffectRenderer(particle, vectors)
    addRenderer(renderer)

    cancel(duration)
  }
}
