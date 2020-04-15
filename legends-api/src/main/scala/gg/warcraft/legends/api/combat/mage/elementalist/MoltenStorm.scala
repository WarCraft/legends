package gg.warcraft.legends.api.combat.mage.elementalist

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Location, Sound, SoundCategory, WorldService}

object MoltenStorm {
  class Factory(
      implicit taskService: TaskService,
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
    ): RangedAbility = new MoltenStorm(caster, snapshot)
  }

  class FireballParticleEffect() extends Effect {}

  class AreaParticleEffect(
      location: Location,
      duration: Duration
  )(
      implicit taskService: TaskService,
      particleAdapter: ParticleAdapter
  ) extends Effect(() => location.add(0, 0.15f, 0)) {
    private final val radius = 7.5f
    private final val particleCount = 100

    private final val particle = Particle.REDSTONE.color(Particle.Color.ORANGE)
    private final val vectors = EffectVectors.ring(radius, particleCount)
    private final val renderer = EffectRenderer(particle, vectors)
    addRenderer(renderer)

    cancel(duration)
  }
}

class MoltenStorm(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    worldService: WorldService
) extends RangedAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_GHAST_SHOOT
  private final val hitSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE

  /*
public class MoltenStorm extends AbstractAbility implements RangedAbility, Runnable {
    private static final Vector3f HEIGHT = new Vector3f(0, 20, 0);
    private static final Vector3f OFFSET = new Vector3f(5, 0, 5);

    private static final Random random = new Random(System.currentTimeMillis());

    private final Hitbox hitbox;
    private final Vector3f velocity;
    private final float range;
    private final Iterator<Location> spawnLocations;

    public MoltenStorm(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
        float spawnRadius = snapshot.getFloat("spawnRadius");
        Duration duration = snapshot.getDuration();
        int fireballsPerSecond = snapshot.getInt("fireballsPerSecond");
        int numberOfFireballs = duration.inSeconds() * fireballsPerSecond;

        Location location = rangedTarget != null
                ? rangedTarget.getLocation()
                : blockTarget.location().toLocation().add(0.5f, 1, 0.5f);

        Location origin = location.add(HEIGHT).add(OFFSET);
        Vector3f direction = location.subtract(origin).translation().normalize();
        float speed = getSnapshot().getFloat(AbilityConfigurationValue.SPEED);
        this.velocity = direction.multiply(speed);
        this.range = getSnapshot().getFloat(AbilityConfigurationValue.RANGE);

        EffectVectors vectors = effectVectorsFactory.createRingVectors(spawnRadius, numberOfFireballs);
        List<Location> spawnLocations = vectors.getVectors().stream()
                .map(vector -> vector.multiply(random.nextFloat()))
                .map(origin::add)
                .collect(Collectors.toList());
        Collections.shuffle(spawnLocations);
        this.spawnLocations = spawnLocations.iterator();

        float radius = getSnapshot().getFloat(AbilityConfigurationValue.RADIUS);
        this.hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());

        // Setup tasks
        Duration fireballPeriod = timeUtils.createDurationInTicks(TimeUtils.TICKS_PER_SECOND / fireballsPerSecond);
        Task task = taskService.runTask(this, timeUtils.oneTick(), fireballPeriod);
        Duration cancelTimer = timeUtils.createDurationInTicks(fireballPeriod.inTicks() * numberOfFireballs);
        taskService.runLater(task::cancel, cancelTimer);

        // Play effect
        mageEffectFactory.createMoltenStormRingEffect(location, duration);

        // Play sound
        WorldService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY);
    }

    @Override
    public void run() {
        Location spawnLocation = spawnLocations.next();
        Movement movement = movementFactory.createLinearMovement(spawnLocation, velocity, range);
        Cancellable effect = mageEffectFactory.createFireballEffect(movement);
        abilityCommandService.createProjectile(this, movement, hitbox, effect::cancel);
    }
}
 */

  override def onHit(entity: Entity): Boolean = {
    entityService damageEntity (entity.id, snapshot.damage)
    entityService burnEntity (entity.id, snapshot.burn)
    worldService playSound (entity.location, hitSound, soundCategory)
    true
  }

  override def onMiss(entity: Entity): Boolean = false

  override def onGrounded(location: Location): Boolean = {
    worldService playSound (location, hitSound, soundCategory)
    true
  }
}
