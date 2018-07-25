package gg.warcraft.legends.app.combat.mage.tempest;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.HitboxFactory;
import gg.warcraft.combat.api.MovementFactory;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.RangedAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.block.Block;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import gg.warcraft.monolith.api.world.service.WorldQueryService;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MoltenStorm extends AbstractAbility implements RangedAbility {
    private static final Vector3fc HEIGHT = new Vector3f(0, 20, 0);
    private static final Vector3fc OFFSET = new Vector3f(5, 0, 5);

    private static final Random random = new Random(System.currentTimeMillis());

    private final Vector3f direction;
    private final List<Location> spawnLocations;

    private int i = 0;
    private BukkitTask task;

    @Inject
    public Bolt(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                WorldQueryService worldQueryService, WorldCommandService worldCommandService,
                HitboxFactory hitboxFactory, MovementFactory movementFactory, @Assisted AbilitySnapshot snapshot,
                @Assisted("caster") Entity caster, @Assisted("target") Entity rangedTarget,
                @Assisted Block blockTarget) {
        super(caster.getId(), snapshot);
        float spawnRadius = snapshot.getFloat("spawnRadius");
        Duration duration = snapshot.getDuration();
    }

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

}
