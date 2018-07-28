package gg.warcraft.legends.app.combat.ranger.hunter;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.BlockLocation;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

public class Trap extends AbstractAbility implements RangedAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound PLACE_SOUND = Sound.BLOCK_IRON_TRAPDOOR_OPEN;
    private static final Sound TRIGGER_SOUND = Sound.BLOCK_IRON_TRAPDOOR_CLOSE;

    private final EntityCommandService entityCommandService;
    private final WorldCommandService worldCommandService;

    @Inject
    public Trap(EntityCommandService entityCommandService, PlayerQueryService playerQueryService,
                WorldCommandService worldCommandService, @Assisted AbilitySnapshot snapshot,
                @Assisted("caster") Entity caster, @Assisted("target") Entity rangedTarget,
                @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.worldCommandService = worldCommandService;

        BlockLocation location = caster.getLocation().toBlockLocation();
        playerQueryService.getOnlinePlayers().stream()
                .filter(player -> player.getTeam() == caster.getTeam())
                .forEach(player -> spoof TRAP_DOOR);
        worldCommandService.playSound(caster.getLocation(), PLACE_SOUND, SOUND_CATEGORY, 1, 1);

        Duration duration = snapshot.getDuration();
        // TODO set up cancel task if nothing happens
    }

    @Override
    public void cancel() {
        tickTask.cancel();
        cancelTask.cancel();
        playerService.getOnlineMonolithPlayers().forEach(player -> {
            player.sendBlockChange(location, location.getBlock().getType(), location.getBlock().getData());
        });
    }

    @Override
    public boolean onHit(Entity entity) {
        PotionEffect poison = getSnapshot().getPotionEffect(PotionEffectType.POISON);
        Duration rootDuration = getSnapshot().getDuration("rootDuration");
        entityCommandService.addPotionEffect(entity.getId(), poison);
        entityCommandService.freeze(entity.getId(), rootDuration);

        worldCommandService.playSound(entity.getLocation(), TRIGGER_SOUND, SOUND_CATEGORY, 1, 1);
        // TODO reset black change for caster
        return true;
    }

    @Override
    public boolean onMiss(Entity entity) {
        worldCommandService.playSound(entity.getLocation(), TRIGGER_SOUND, SOUND_CATEGORY, 1, 1);
        // TODO reset black change for caster
        return true;
    }
}
