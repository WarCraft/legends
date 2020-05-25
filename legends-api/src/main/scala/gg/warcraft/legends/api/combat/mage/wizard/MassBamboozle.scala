package gg.warcraft.legends.api.combat.mage.wizard

import java.util.UUID

import gg.warcraft.combat.api.Movement
import gg.warcraft.combat.api.ability.{AbilityPreCastEvent, RangedAbility}
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.{
  Effect, EffectRenderer, EffectVectors, Particle, ParticleAdapter
}
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.math.Vector3f
import gg.warcraft.monolith.api.util.typing._
import gg.warcraft.monolith.api.world.WorldService

class MassBamboozle(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService
) extends RangedAbility {
  override def onHit(entity: Entity): Boolean = ???
  override def onMiss(entity: Entity): Boolean = ???
}

/*
public class MassBamboozle extends AbstractAbility implements RangedAbility {
    private static final Random random;
    private static final List<MobDisguise> disguises;

    static {
        random = new Random(System.currentTimeMillis());
        disguises = new ArrayList<>();
        disguises.add(new MobDisguise(DisguiseType.COW));
        disguises.add(new MobDisguise(DisguiseType.PIG));
        disguises.add(new MobDisguise(DisguiseType.SHEEP));
    }

    private final Server server;
    private final MassBamboozleRepository massBamboozleRepository;
    private final TaskService taskService;

    @Inject
    public MassBamboozle(Server server, AbilityCommandService abilityCommandService, MovementFactory movementFactory,
                         HitboxFactory hitboxFactory, MageEffectFactory mageEffectFactory, TaskService taskService,
                         MassBamboozleRepository massBamboozleRepository,
                         @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                         @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.server = server;
        this.massBamboozleRepository = massBamboozleRepository;
        this.taskService = taskService;
        Location location;
        if (rangedTarget != null) {
            location = rangedTarget.getLocation();
        } else if (blockTarget != null) {
            location = blockTarget.location().toLocation().add(0.5f, 1, 0.5f);
        } else {
            throw new IllegalStateException("Failed to cast Mass Bamboozle with null entity and block targets");
        }

        CombatValue radius = snapshot.getValue(AbilityConfigurationValue.RADIUS);
        Movement movement = movementFactory.createOneOffMovement(location);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius.getModifiedValue(), entity -> entity.getTeam() != caster.getTeam());

        Cancellable effect = mageEffectFactory.createMassBamboozleCountdownEffect(location);
        Duration countdown = snapshot.getDuration("countdown");
        taskService.runLater(() -> {
            effect.cancel();
            abilityCommandService.createProjectile(this, movement, hitbox, () -> {});
        }, countdown);
    }

    private MobDisguise getRandomDisguise() {
        int disguiseCount = disguises.size();
        int randomIndex = random.nextInt(disguiseCount);
        return disguises.get(randomIndex);
    }

    @Override
    public boolean onHit(Entity entity) {
        UUID entityId = entity.getId();

        MobDisguise randomDisguise = getRandomDisguise();
        org.bukkit.entity.Entity spigotEntity = server.getEntity(entityId);
        DisguiseAPI.disguiseToAll(spigotEntity, randomDisguise);

        massBamboozleRepository.save(entityId);
        Duration duration = getSnapshot().getDuration();
        taskService.runLater(() -> {
            massBamboozleRepository.delete(entityId);
            DisguiseAPI.undisguiseToAll(spigotEntity);
        }, duration);

        return false;
    }

    @Override
    public boolean onMiss(Entity entity) {
        // do nothing
        return false;
    }
}

 */

object MassBamboozle {
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
    ): RangedAbility = new MassBamboozle(caster, snapshot)
  }

  class ParticleEffect(movement: Movement)(implicit
      particleAdapter: ParticleAdapter,
      taskService: TaskService
  ) extends Effect(() => movement.get + Vector3f.Offset.feet) {
    private final val radius = 5f
    private final val particleCount = 80

    private val particle = Particle.DragonBreath
    private val vectors = EffectVectors.ring(radius, particleCount)
    private val renderer = EffectRenderer(particle, vectors)
    addRenderer(renderer)
  }

  class Repository {
    private var _bamboozled: Set[UUID] = Set.empty

    def bamboozled: Set[UUID] = _bamboozled
    def save(entityId: UUID): Unit = _bamboozled += entityId
    def remove(entityId: UUID): Unit = _bamboozled -= entityId
  }

  class Handler(repository: Repository) extends Event.Handler {
    override def reduce[T <: PreEvent](event: T): T = event match {
      case event @ AbilityPreCastEvent(_, caster, _, _, _) =>
        if (!repository.bamboozled.contains(caster.id)) event.as[T]
        else event.copy(cancelled = true).as[T]
      case _ => event
    }
  }
}
