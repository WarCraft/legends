package gg.warcraft.legends.app.combat.ranger.hunter;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Hitbox;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.BlockLocation;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.*;
import gg.warcraft.monolith.api.world.block.spoofing.BlockSpoofingCommandService;
import gg.warcraft.monolith.api.world.WorldService;
import gg.warcraft.monolith.api.world.block.variant.TrapdoorVariant;

import javax.annotation.Nullable;
import java.util.UUID;

public class Trap extends AbstractAbility implements RangedAbility, Cancellable {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound PLACE_SOUND = Sound.BLOCK_IRON_TRAPDOOR_OPEN;
    private static final Sound TRIGGER_SOUND = Sound.BLOCK_IRON_TRAPDOOR_CLOSE;

    private final AbilityCommandService abilityCommandService;
    private final EntityCommandService entityCommandService;
    private final WorldService WorldService;
    private final BlockSpoofingCommandService blockSpoofingCommandService;
    private final TaskService taskService;
    private final BlockLocation blockLocation;
    private final UUID[] spoofedPlayerIds;
    private final UUID projectileId;

    private boolean cancelled = false;

    @Inject
    public Trap(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                PlayerQueryService playerQueryService, WorldService WorldService,
                BlockSpoofingCommandService blockSpoofingCommandService,
                MovementFactory movementFactory, HitboxFactory hitboxFactory, TaskService taskService,
                @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.abilityCommandService = abilityCommandService;
        this.entityCommandService = entityCommandService;
        this.WorldService = WorldService;
        this.blockSpoofingCommandService = blockSpoofingCommandService;
        this.taskService = taskService;

        this.blockLocation = caster.getLocation().toBlockLocation();
        Block currentBlock = WorldService.getBlock(blockLocation);
        Block fakeBlock = new Trapdoor(currentBlock.location(), TrapdoorVariant.OAK, BlockFace.NORTH, BlockBisection.TOP,
                false, false, false);
        this.spoofedPlayerIds = playerQueryService.getOnlinePlayers().stream()
                .filter(player -> player.getTeam() == caster.getTeam())
                .map(Player::getId)
                .toArray(UUID[]::new);
        blockSpoofingCommandService.spoofBlock(fakeBlock, spoofedPlayerIds);

        Location trapLocation = blockLocation.toLocation().add(0.5f, 0, 0.5f);
        Movement movement = movementFactory.createStationaryMovement(trapLocation);
        CombatValue radius = snapshot.getValue(AbilityConfigurationValue.RADIUS);
        Hitbox hitbox = hitboxFactory.createSphericalHitbox(radius.getModifiedValue(), entity -> entity.getTeam() != caster.getTeam());
        projectileId = abilityCommandService.createProjectile(this, movement, hitbox, () -> {});

        Duration duration = snapshot.getDuration();
        taskService.runLater(this::cancel, duration);
        WorldService.playSound(caster.getLocation(), PLACE_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void cancel() {
        if (!cancelled) {
            cancelled = true;
            abilityCommandService.cancelProjectile(projectileId);
            blockSpoofingCommandService.unspoofBlock(blockLocation, spoofedPlayerIds);
        }
    }

    @Override
    public boolean onHit(Entity entity) {
        UUID entityId = entity.getId();
        Location startLocation = entity.getLocation();
        PotionEffect poison = getSnapshot().getPotionEffect(PotionEffectType.POISON);
        Duration rootDuration = getSnapshot().getDuration("root-duration");
        entityCommandService.addPotionEffect(entityId, poison);
        entityCommandService.freeze(entityId, rootDuration);

        // yeet-into-space fix
        taskService.runNextTick(() -> {
            entityCommandService.teleport(entityId, startLocation);
            entityCommandService.setVelocity(entityId, new Vector3f());
        });

        cancel();
        WorldService.playSound(entity.getLocation(), TRIGGER_SOUND, SOUND_CATEGORY, 1, 1);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        cancel();
        WorldService.playSound(entity.getLocation(), TRIGGER_SOUND, SOUND_CATEGORY, 1, 1);
        return true;
    }

    @Override
    public boolean onGrounded(Location location) {
        // do nothing
        return false;
    }
}
