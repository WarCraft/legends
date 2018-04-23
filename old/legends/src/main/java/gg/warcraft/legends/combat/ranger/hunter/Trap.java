package gg.warcraft.legends.combat.ranger.hunter;

import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.player.PlayerService;
import gg.warcraft.monolith.util.Cancellable;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.PotionConfiguration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class Trap extends SimpleMonolithAbility implements Runnable, Cancellable {

    private final EntityService entityService;
    private final PlayerService playerService;
    private final Location location;
    private final Predicate<MonolithEntity> isHostile;
    private final double radius;
    private final PotionConfiguration poison;
    private final Duration rootDuration;
    private final BukkitTask tickTask;
    private final BukkitTask cancelTask;

    public Trap(MonolithEntity caster, double radius,
                Duration trapDuration, Duration rootDuration, PotionConfiguration poison) {
        this(EntityService.getInstance(), PlayerService.getInstance(), caster, radius, trapDuration, rootDuration, poison);
    }

    public Trap(EntityService entityService, PlayerService playerService, MonolithEntity caster, double radius,
                Duration trapDuration, Duration rootDuration, PotionConfiguration poison) {
        super(caster);
        this.entityService = entityService;
        this.playerService = playerService;
        this.location = caster.getLocation();
        this.isHostile = entity -> entity.getRace() != caster.getRace();
        this.radius = radius;
        this.poison = poison;
        this.rootDuration = rootDuration;
        this.tickTask = RunnableUtils.runTaskEachTick(this);
        this.cancelTask = RunnableUtils.runLater(this::cancel, trapDuration.as(TICKS));

        // (or) entityService.getNearbyMonolithEntities(caster.getLocation(), 100)
        playerService.getOnlineMonolithPlayers().stream()
                .filter(player -> player.getRace() == caster.getRace())
                .forEach(player -> player.sendBlockChange(this.location, Material.TRAP_DOOR, (byte) 0));

        location.getWorld().playSound(location, Sound.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.HOSTILE, 1, 1);
    }

    @Override
    public String toString() {
        return "Trap";
    }

    @Override
    public void run() {
        List<MonolithEntity> casualties = entityService.getNearbyMonolithEntities(location, radius).stream()
                .filter(isHostile)
                .collect(Collectors.toList());
        if (!casualties.isEmpty()) {
            location.getWorld().playSound(location, Sound.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.HOSTILE, 1, 1);
            casualties.forEach(entity -> {
                entity.addPotionEffect(PotionEffectType.POISON, poison);
                entity.freeze((int) rootDuration.as(TICKS));
            });
            cancel();
        }
    }

    @Override
    public void cancel() {
        tickTask.cancel();
        cancelTask.cancel();
        playerService.getOnlineMonolithPlayers().forEach(player -> {
            player.sendBlockChange(location, location.getBlock().getType(), location.getBlock().getData());
        });
    }
}
