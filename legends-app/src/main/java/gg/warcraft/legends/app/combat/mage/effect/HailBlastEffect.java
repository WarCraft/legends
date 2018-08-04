package gg.warcraft.legends.app.combat.mage.effect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.monolith.api.effect.Effect;
import gg.warcraft.monolith.api.effect.EffectFactory;
import gg.warcraft.monolith.api.effect.EffectRenderer;
import gg.warcraft.monolith.api.effect.EffectRendererFactory;
import gg.warcraft.monolith.api.effect.EffectVectors;
import gg.warcraft.monolith.api.effect.EffectVectorsFactory;
import gg.warcraft.monolith.api.effect.Particle;
import gg.warcraft.monolith.api.effect.ParticleFactory;
import gg.warcraft.monolith.api.effect.ParticleType;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.Location;

public class HailBlastEffect implements Runnable, Cancellable {
    private static final int ITERATIONS = 15;

    private final EffectVectorsFactory effectVectorsFactory;
    private final EffectRendererFactory effectRendererFactory;
    private final Movement movement;
    private final Particle particle;
    private final Effect effect;

    private int iteration;

    @Inject
    public HailBlastEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                           EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                           TimeUtils timeUtils, @Assisted Movement movement) {
        this.effectVectorsFactory = effectVectorsFactory;
        this.effectRendererFactory = effectRendererFactory;
        this.movement = movement;

        this.effect = effectFactory.createDynamicEffect(movement, this);
        Duration duration = timeUtils.createDurationInTicks(ITERATIONS);
        this.effect.cancel(duration);

        this.particle = particleFactory.createSimpleParticle(ParticleType.CLOUD);
        this.iteration = 1;
    }

    @Override
    public void run() {
        effect.clearRenderers();

        Location currentLocation = movement.get();
        EffectVectors circleVectors = effectVectorsFactory.createCircleVectors(0.4f + 0.2f * iteration, 4 * iteration);
        // TODO add velocity to Movement and research way to grab pitch and yaw from vector3f?
        // circleVectors.rotateAroundAxisX(Math.toRadians(90 + currentLocation.getPitch()));
        // circleVectors.rotateAroundAxisY(Math.toRadians(-currentLocation.getYaw()));
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, circleVectors);
        effect.addRenderer(renderer);

        effect.run();
        iteration += 1;
    }

    @Override
    public void cancel() {
        // FIXME is this necessary?
    }
}

/*
@Inject
    public MoltenStormCircleEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                                   EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                                   @Assisted Location location, @Assisted Duration duration) {
        Location adjustedLocation = location.add(0, 0.15f, 0);
        this.effect = effectFactory.createEffect(() -> adjustedLocation, duration);

        Particle particle = particleFactory.createColorParticle(ParticleType.REDSTONE, ColorHue.ORANGE);
        EffectVectors vectors = effectVectorsFactory.createRingVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, vectors);
        this.effect.addRenderer(renderer);
    }

 */