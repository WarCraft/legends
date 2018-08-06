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
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.BlockLocation;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.block.BlockUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FlameTotem extends AbstractAbility implements RangedAbility, Runnable {
    private static final float MAX_DISTANCE = 30;

    private final AbilityCommandService abilityCommandService;
    private final EntityCommandService entityCommandService;
    private final MovementFactory movementFactory;
    private final HitboxFactory hitboxFactory;
    private final MageEffectFactory mageEffectFactory;
    private final Task task;

    @Inject
    public FlameTotem(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                      TaskService taskService, MovementFactory movementFactory, HitboxFactory hitboxFactory,
                      MageEffectFactory mageEffectFactory, BlockUtils blockUtils, TimeUtils timeUtils,
                      @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                      @Assisted("target") Entity rangedTarget, @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);
        this.abilityCommandService = abilityCommandService;
        this.entityCommandService = entityCommandService;
        this.movementFactory = movementFactory;
        this.hitboxFactory = hitboxFactory;
        this.mageEffectFactory = mageEffectFactory;

        BlockLocation casterLocation = caster.getLocation().toBlockLocation();
        Set<Block> adjacentBlocksX = blockUtils.getAdjacentBlocksX(casterLocation);
        Set<Block> surroundingBlocksXZ = new HashSet<>(adjacentBlocksX);
        adjacentBlocksX.forEach(block -> surroundingBlocksXZ.addAll(blockUtils.getAdjacentBlocksZ(block)));
        surroundingBlocksXZ.addAll(blockUtils.getAdjacentBlocksZ(casterLocation));
        Optional<Block> spawnBlockOptional = surroundingBlocksXZ.stream()
                .filter(block -> block.getType() == BlockType.AIR)
                .findFirst();
        Block totem = spawnBlockOptional.orElse(caster.getLocation().getBlock());
        totemBackup = worldService.createBackup(totem);

        totem.setType(BlockType.REDSTONE_TORCH_ON);


        task = taskService.runTask(this, timeUtils.oneTick(), snapshot.getPeriod());
        taskService.runLater(() -> {
            task.cancel();
            totemBackup.restore();
        }, snapshot.getDuration());


        // TODO play cast sound
    }

    @Override
    public void run() {
        float radius = getSnapshot().getFloat(AbilityConfigurationValue.RADIUS);
        Movement movement = movementFactory.createLinearMovement(location, direction, MAX_DISTANCE);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius, entity -> entity.getTeam() != caster.getTeam());

        Cancellable effect = mageEffectFactory.createFlameTotemFireballEffect(movement);
        abilityCommandService.createProjectile(this, movement, hitbox, effect::cancel);

//        List<MonolithEntity> nearbyEntities = entityService.getNearbyMonolithEntities(totem.getLocation(), 30).stream()
//                .filter(entity -> entity.getRace() != caster.getRace())
//                .collect(Collectors.toList());
//        if (nearbyEntities.isEmpty()) {
//            return;
//        }
//        int randomTargetNumber = random.nextInt(nearbyEntities.size());
//        MonolithEntity randomTarget = nearbyEntities.get(randomTargetNumber);
//
//        Vector targetLocationVector = randomTarget.getLocation().add(0, 0.5, 0).toVector();
//        Vector totemLocationVector = totem.getLocation().add(0.5, 0.5, 0.5).toVector();
//        Vector castDirection = targetLocationVector.subtract(totemLocationVector).normalize();
//        Vector halfCastDirection = castDirection.clone().multiply(0.5);
//        totemFireballFactory.create(caster, totem.getLocation().clone().add(0.5, 0.5, 0.5).add(halfCastDirection), castDirection);
    }

    @Override
    public boolean onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        Duration burnDuration = getSnapshot().getDuration(AbilityConfigurationEffect.BURN);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.burn(entity.getId(), burnDuration);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        return false;
    }
}
