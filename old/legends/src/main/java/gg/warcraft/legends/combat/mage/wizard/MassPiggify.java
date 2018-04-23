package gg.warcraft.legends.combat.mage.wizard;

import gg.warcraft.legends.combat.effect.mage.wizard.MassPigifyCountdownEffect;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.combat.HostileTo;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.effect.MonolithEffect;
import gg.warcraft.monolith.effect.vectors.RingVectors;
import gg.warcraft.monolith.entity.EntityService;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.entity.SimpleMonolithEntity;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static gg.warcraft.monolith.util.Duration.Type.MILLIS;
import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class MassPiggify extends SimpleMonolithAbility {
    private static final int PIG_TARGET_COUNT = 7;

    private static final Plugin monolith = Monolith.getInstance();
    private static final Random random = new Random(System.currentTimeMillis());

    static {
        EventService.getInstance().registerForEvents(new Listener() {
            @EventHandler
            public void onVehicleExitEvent(VehicleExitEvent event) {
                Entity entity = event.getExited();
                List<MetadataValue> metadata = entity.getMetadata(MassPiggify.class.getCanonicalName());
                if (metadata.isEmpty()) {
                    return;
                }

                MetadataValue value = metadata.get(0);
                if (value.asLong() < System.currentTimeMillis()) {
                    entity.removeMetadata(MassPiggify.class.getCanonicalName(), monolith);
                } else {
                    Location tpLocation = event.getExited().getLocation().clone();
                    tpLocation.setDirection(entity.getLocation().getDirection());
                    RunnableUtils.runOnNextTick(() -> {
                        entity.teleport(tpLocation);
                        RunnableUtils.runOnNextTick(() -> event.getExited().addPassenger(entity));
                    });
                }
            }
        });
    }

    private final EntityService entityService;
    private final Location location;
    private final double radius;
    private final Duration duration;

    private List<MonolithEntity> casualties;
    private List<Location> targets;
    private List<Pig> pigs;
    private BukkitTask randomizePigTargetsTask;
    private BukkitTask playPigNoisesTask;

    public MassPiggify(MonolithEntity caster, Location location, Duration countdown, double radius, Duration duration) {
        this(EntityService.getInstance(), caster, location, countdown, radius, duration);
    }

    public MassPiggify(EntityService entityService, MonolithEntity caster, Location location, Duration countdown,
                       double radius, Duration duration) {
        super(caster);
        this.entityService = entityService;
        this.location = location;
        this.radius = radius;
        this.duration = duration;

        this.targets = new ArrayList<>();
        this.pigs = new ArrayList<>();

        MonolithEffect effect = new MassPigifyCountdownEffect(location);
        RunnableUtils.runLater(() -> {
            effect.cancel();
            spawnPigs();
        }, countdown.as(TICKS));
    }

    private void spawnPigs() {
        casualties = entityService.getNearbyMonolithEntities(location, radius).stream()
                .filter(new HostileTo(getCaster()))
                .collect(Collectors.toList());
        if (casualties.isEmpty()) {
            return;
        }

        RingVectors circle = new RingVectors(radius * 2, PIG_TARGET_COUNT);
        circle.iterator().forEachRemaining(vector -> {
            Location loc = location.add(vector);
            targets.add(loc);
            loc.subtract(vector);
        });

        casualties.forEach(entity -> {
            Pig pig = entity.getWorld().spawn(entity.getLocation(), Pig.class);
            pigs.add(pig);
            pig.setSaddle(true);
            pig.addPassenger(((SimpleMonolithEntity) entity).getLivingEntity()); // FIXME remove cast to SME hack, cannot cast MonolithEntity even if legal

            long targetTime = System.currentTimeMillis() + duration.as(MILLIS);
            MetadataValue metadataValue = new FixedMetadataValue(monolith, targetTime);
            entity.setMetadata(MassPiggify.class.getCanonicalName(), metadataValue);
        });
        RunnableUtils.runLater(this::removePigs, duration.as(TICKS));
        randomizePigTargetsTask = RunnableUtils.runTask(this::randomizePigTargets, 1, TimeUtils.TICKS_PER_SECOND);
        playPigNoisesTask = RunnableUtils.runTask(() -> {
            location.getWorld().playSound(location, Sound.ENTITY_PIG_AMBIENT, SoundCategory.HOSTILE, 1, 1);
        }, 1, 10);
    }

    private void randomizePigTargets() {
        Location target = targets.get(random.nextInt(targets.size()));
        double x = target.getX();
        double y = target.getY();
        double z = target.getZ();
        pigs.forEach(pig -> ((CraftPig) pig).getHandle().getNavigation().a(x, y, z, 1.5));
    }

    private void removePigs() {
        randomizePigTargetsTask.cancel();
        playPigNoisesTask.cancel();
        casualties.forEach(entity -> entity.removeMetadata(MassPiggify.class.getCanonicalName(), monolith));
        pigs.forEach(Pig::remove);
    }

    @Override
    public String toString() {
        return "Mass Piggify";
    }
}
