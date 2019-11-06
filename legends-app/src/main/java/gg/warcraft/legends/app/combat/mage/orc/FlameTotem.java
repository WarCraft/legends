package gg.warcraft.legends.app.combat.mage.orc;

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
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.BlockLocation;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.block.BlockUtils;
import gg.warcraft.monolith.api.world.block.backup.BlockBackupService;
import gg.warcraft.monolith.api.world.WorldService;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
