package gg.warcraft.legends.app.combat.mage.tempest;

import gg.warcraft.legends.combat.effect.mage.tempest.MoltenStormImpactEffect;
import gg.warcraft.monolith.combat.SimpleMonolithAbility;
import gg.warcraft.monolith.config.AbilityConfiguration;
import gg.warcraft.monolith.config.ConfigurationService;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithEffect;
import gg.warcraft.monolith.effect.vectors.RingVectors;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static gg.warcraft.monolith.util.Duration.Type.SECONDS;
import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class MoltenStorm extends SimpleMonolithAbility {
    private static final Vector HEIGHT = new Vector(0, 20, 0);
    private static final Vector OFFSET = new Vector(5, 0, 5);

    private static final Random random = new Random(System.currentTimeMillis());

    private final Vector direction;
    private final List<Location> spawnLocations;

    private int i = 0;
    private BukkitTask task;

    public MoltenStorm(MonolithEntity caster, AbilityConfiguration config, Location location) {
        super(caster, config);
        double spawnRadius = (double) config.get("spawnRadius");
        Duration duration = config.getDuration();

        Location origin = location.clone().add(HEIGHT).add(OFFSET);
        this.direction = location.clone().subtract(origin).toVector().normalize();
        EffectVectors vectors = new RingVectors(spawnRadius, (int) duration.as(SECONDS) * 4);
        vectors.forEach(vector -> vector.multiply(random.nextDouble())); // TODO update to new CircleVectors
        this.spawnLocations = new ArrayList<>();
        vectors.forEach(vector -> spawnLocations.add(origin.clone().add(vector)));
        Collections.shuffle(spawnLocations);

        task = RunnableUtils.runTask(this::spawnFireball, 1, 5);
        RunnableUtils.runLater(task::cancel, duration.as(TICKS));

        MonolithEffect groundEffect = new MoltenStormImpactEffect(location.clone());
        RunnableUtils.runLater(groundEffect::cancel, duration.as(TICKS));
    }

    private void spawnFireball() {
        AbilityConfiguration config = ConfigurationService.getInstance().getAbilityConfigurationFor(MoltenStorm.class); // TODO fix having to call ConfigService from within spell (add clone method to config?)
        new MoltenFireball(getCaster(), config, spawnLocations.get(i), direction);
        i += 1;
    }

    @Override
    public String toString() {
        return "Molten Storm";
    }
}
