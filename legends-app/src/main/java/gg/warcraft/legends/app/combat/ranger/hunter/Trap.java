package gg.warcraft.legends.app.combat.ranger.hunter;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.block.spoofing.BlockSpoofingCommandService;
import gg.warcraft.monolith.api.world.location.BlockLocation;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import gg.warcraft.monolith.api.world.service.WorldQueryService;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

public class Trap extends AbstractAbility implements RangedAbility, Cancellable {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound PLACE_SOUND = Sound.BLOCK_IRON_TRAPDOOR_OPEN;
    private static final Sound TRIGGER_SOUND = Sound.BLOCK_IRON_TRAPDOOR_CLOSE;

    private final EntityCommandService entityCommandService;
    private final WorldCommandService worldCommandService;
    private final BlockSpoofingCommandService blockSpoofingCommandService;
    private final BlockLocation blockLocation;
    private final UUID[] spoofedPlayerIds;

    @Inject
    public Trap(EntityCommandService entityCommandService, PlayerQueryService playerQueryService,
                WorldCommandService worldCommandService, WorldQueryService worldQueryService,
                BlockSpoofingCommandService blockSpoofingCommandService, TaskService taskService,
                @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                @Assisted("target") @Nullable Entity rangedTarget, @Assisted @Nullable Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.worldCommandService = worldCommandService;
        this.blockSpoofingCommandService = blockSpoofingCommandService;

        this.blockLocation = caster.getLocation().toBlockLocation();
        Block currentBlock = worldQueryService.getBlockAt(blockLocation);
        Block fakeBlock = currentBlock.withType(BlockType.WOODEN_TRAPDOOR);
        this.spoofedPlayerIds = playerQueryService.getOnlinePlayers().stream()
                .filter(player -> player.getTeam() == caster.getTeam())
                .toArray(UUID[]::new);
        blockSpoofingCommandService.spoofBlock(fakeBlock, spoofedPlayerIds);
        worldCommandService.playSound(caster.getLocation(), PLACE_SOUND, SOUND_CATEGORY, 1, 1);

        Duration duration = snapshot.getDuration();
        taskService.runLater(this::cancel, duration);
    }

    @Override
    public void cancel() {
        Arrays.stream(spoofedPlayerIds).forEach(id -> blockSpoofingCommandService.unspoofBlock(blockLocation));
    }

    @Override
    public boolean onHit(Entity entity) {
        PotionEffect poison = getSnapshot().getPotionEffect(PotionEffectType.POISON);
        Duration rootDuration = getSnapshot().getDuration("root-duration");
        entityCommandService.addPotionEffect(entity.getId(), poison);
        entityCommandService.freeze(entity.getId(), rootDuration);

        cancel();
        worldCommandService.playSound(entity.getLocation(), TRIGGER_SOUND, SOUND_CATEGORY, 1, 1);
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        cancel();
        worldCommandService.playSound(entity.getLocation(), TRIGGER_SOUND, SOUND_CATEGORY, 1, 1);
        return true;
    }
}
