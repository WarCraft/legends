package gg.warcraft.legends.app.combat.mage.tempest;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Hitbox;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationEffect;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.legends.app.combat.mage.effect.MageEffectFactory;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.core.Task;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.effect.EffectVectors;
import gg.warcraft.monolith.api.effect.EffectVectorsFactory;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MoltenStorm extends AbstractAbility implements RangedAbility, Runnable {
    private static final Vector3f HEIGHT = new Vector3f(0, 20, 0);
    private static final Vector3f OFFSET = new Vector3f(5, 0, 5);
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_GHAST_SHOOT;
    private static final Sound HIT_SOUND = Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE;

    private static final Random random = new Random(System.currentTimeMillis());

    private final AbilityCommandService abilityCommandService;
    private final EntityCommandService entityCommandService;
    private final WorldCommandService worldCommandService;
    private final MovementFactory movementFactory;
    private final MageEffectFactory mageEffectFactory;
    private final Hitbox hitbox;
    private final Vector3f velocity;
    private final float range;
    private final Iterator<Location> spawnLocations;

    @Inject
    public MoltenStorm(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                       WorldCommandService worldCommandService, TaskService taskService, TimeUtils timeUtils,
                       HitboxFactory hitboxFactory, MovementFactory movementFactory,
                       EffectVectorsFactory effectVectorsFactory, MageEffectFactory mageEffectFactory,
                       @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                       @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.abilityCommandService = abilityCommandService;
        this.entityCommandService = entityCommandService;
        this.worldCommandService = worldCommandService;
        this.movementFactory = movementFactory;
        this.mageEffectFactory = mageEffectFactory;

        float spawnRadius = snapshot.getFloat("spawnRadius");
        Duration duration = snapshot.getDuration();
        int fireballsPerSecond = snapshot.getInt("fireballsPerSecond");
        int numberOfFireballs = duration.inSeconds() * fireballsPerSecond;

        Location location = rangedTarget != null
                ? rangedTarget.getLocation()
                : blockTarget.getLocation().toLocation().add(0.5f, 1, 0.5f);

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
        worldCommandService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void run() {
        Location spawnLocation = spawnLocations.next();
        Movement movement = movementFactory.createLinearMovement(spawnLocation, velocity, range);
        Cancellable effect = mageEffectFactory.createFireballEffect(movement);
        abilityCommandService.createProjectile(this, movement, hitbox, effect::cancel);
    }

    @Override
    public boolean onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        Duration burnDuration = getSnapshot().getDuration(AbilityConfigurationEffect.BURN);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.burn(entity.getId(), burnDuration);

        worldCommandService.playSound(entity.getLocation(), HIT_SOUND, SOUND_CATEGORY, 1, 1);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }

    @Override
    public boolean onGrounded(Location location) {
        worldCommandService.playSound(location, HIT_SOUND, SOUND_CATEGORY, 1, 1);
        return true;
    }
}
