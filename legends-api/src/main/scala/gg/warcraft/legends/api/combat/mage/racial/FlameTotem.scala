package gg.warcraft.legends.api.combat.mage.racial

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.Entity

object FlameTotem {
  class Factory extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new FlameTotem()
  }
}

class FlameTotem(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
) extends RangedAbility {
  override def onHit(entity: Entity): Boolean = ???
  override def onMiss(entity: Entity): Boolean = ???
}


/*
public interface FlameTotemFactory extends RangedAbilityFactory {

    @Override
    default EnumSet<RangedAbilityFlag> getFlags() {
        return EnumSet.noneOf(RangedAbilityFlag.class);
    }
}

 */



/*
public class OrcMagePassive extends AbstractRacialPassive {
    private static final String NAME = "Flame Totem";
    private static final String ABILITY_NAME = "Flame Totem";

    private final AbilityCommandService abilityCommandService;

    @Inject
    public OrcMagePassive(AbilityCommandService abilityCommandService,
                          @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.abilityCommandService = abilityCommandService;
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        if (event.getNewPercentHealth() <= 0.75) {
            abilityCommandService.castAbility(ABILITY_NAME, getEntityId(), null);
        }
    }
}
 */







/*
public class FlameTotem extends AbstractAbility implements RangedAbility, Runnable {
    private static final float MAX_DISTANCE = 30;
    private static final Random random = new Random(System.currentTimeMillis());

    private final AbilityCommandService abilityCommandService;
    private final EntityQueryService entityQueryService;
    private final EntityCommandService entityCommandService;
    private final MovementFactory movementFactory;
    private final HitboxFactory hitboxFactory;
    private final MageEffectFactory mageEffectFactory;

    private final Team casterTeam;
    private final Location totemLocation;
    private final Task task;

    @Inject
    public FlameTotem(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                      EntityQueryService entityQueryService, WorldService WorldService, BlockBackupService blockBackupService,
                      TaskService taskService, MovementFactory movementFactory, HitboxFactory hitboxFactory,
                      MageEffectFactory mageEffectFactory, BlockUtils blockUtils, TimeUtils timeUtils,
                      @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                      @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.abilityCommandService = abilityCommandService;
        this.entityQueryService = entityQueryService;
        this.entityCommandService = entityCommandService;
        this.movementFactory = movementFactory;
        this.hitboxFactory = hitboxFactory;
        this.mageEffectFactory = mageEffectFactory;
        this.casterTeam = caster.getTeam();

        BlockLocation casterLocation = caster.getLocation().toBlockLocation();
        Block casterLocationBlock = WorldService.getBlock(casterLocation);
        Set<Block> adjacentBlocksX = blockUtils.getAdjacentBlocksX(casterLocationBlock);
        Set<Block> surroundingBlocksXZ = new HashSet<>(adjacentBlocksX);
        adjacentBlocksX.forEach(block -> surroundingBlocksXZ.addAll(blockUtils.getAdjacentBlocksZ(block)));
        surroundingBlocksXZ.addAll(blockUtils.getAdjacentBlocksZ(casterLocationBlock));
        Optional<Block> spawnBlockOptional = surroundingBlocksXZ.stream()
                .filter(block -> block.type() == BlockType.AIR)
                .findFirst();
        Block totemBlock = spawnBlockOptional.orElse(casterLocationBlock);
        BlockLocation totemLocation = totemBlock.location();
        UUID totemBlockBackupId = !totemBlock.liquid()
                ? blockBackupService.createBackup(totemLocation)
                : null;
        if (totemBlockBackupId != null) {
            WorldService.setBlock(totemBlock.location(), BlockType.REDSTONE_TORCH); // TODO RedstoneTorchState:LIT
        }
        this.totemLocation = totemLocation
                .toLocation()
                .add(0.5f, 0.5f, 0.5f);

        this.task = taskService.runTask(this, timeUtils.oneTick(), snapshot.getDuration("period"));
        taskService.runLater(() -> {
            task.cancel();
            if (totemBlockBackupId != null) {
                blockBackupService.restoreBackup(totemBlockBackupId);
            }
        }, snapshot.getDuration());

        // TODO play cast sound
    }

    @Override
    public void run() {
        List<Entity> nearbyEntities = entityQueryService.getNearbyEntities(totemLocation, 30).stream()
                .filter(entity -> entity.getTeam() != casterTeam)
                .collect(Collectors.toList());
        if (nearbyEntities.isEmpty()) {
            return;
        }

        int randomTargetNumber = random.nextInt(nearbyEntities.size());
        Entity randomTarget = nearbyEntities.get(randomTargetNumber);

        Location targetLocation = randomTarget.getLocation().add(0, 0.5f, 0);
        Vector3f castDirection = targetLocation.subtract(totemLocation).translation().normalize();
        Vector3f halfCastDirection = castDirection.multiply(0.5f);
        Location fireballOrigin = totemLocation.add(halfCastDirection);

        float radius = getSnapshot().getFloat(AbilityConfigurationValue.RADIUS);
        Movement movement = movementFactory.createLinearMovement(fireballOrigin, castDirection, MAX_DISTANCE);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != casterTeam);

        Cancellable effect = mageEffectFactory.createFlameTotemFireballEffect(movement);
        abilityCommandService.createProjectile(this, movement, hitbox, effect::cancel);
    }

    @Override
    public boolean onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        Duration burnDuration = getSnapshot().getDuration(AbilityConfigurationEffect.BURN);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.burn(entity.getId(), burnDuration);

        // TODO play sound
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }

    @Override
    public boolean onGrounded(Location location) {
        return false;
    }
}

 */








/*
public class FlameTotemFireballEffect implements Cancellable {
    private static final float RADIUS = 0.1f;
    private static final int PARTICLE_COUNT = 8;

    private final Effect effect;

    @Inject
    public FlameTotemFireballEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                                    EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                                    @Assisted Movement movement) {
        this.effect = effectFactory.createSimpleEffect(movement);

        Particle innerParticle = particleFactory.createSimpleParticle(ParticleType.FLAME);
        EffectVectors innerVectors = effectVectorsFactory.createOriginVector();
        EffectRenderer innerRenderer = effectRendererFactory.createSimpleEffectRenderer(innerParticle, innerVectors);
        this.effect.addRenderer(innerRenderer);

        Particle outerParticle = particleFactory.createColorParticle(ParticleType.REDSTONE, ColorHue.ORANGE);
        EffectVectors outerVectors = effectVectorsFactory.createSphereVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer outerRenderer = effectRendererFactory.createSimpleEffectRenderer(outerParticle, outerVectors);
        this.effect.addRenderer(outerRenderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}

 */





/*
public class FlameTotemDamageLimitationHandler implements CombatSource {
    private static final String FLAME_TOTEM = "Flame Totem";

    private final CombatValueModifier modifier;

    @Inject
    public FlameTotemDamageLimitationHandler(CombatFactory combatFactory) {
        this.modifier = combatFactory.createCombatValueModifier(
                CombatValueModifierType.OVERRIDE, 2f, this);
    }

    @Override
    public String getName() {
        return FLAME_TOTEM;
    }

    @Override
    public UUID getEntityId() {
        return null;
    }

    @Subscribe
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        if ("Flame Totem".equals(event.getAbilityName())) {
            event.addDamageModifier(modifier);
        }
    }
}

 */