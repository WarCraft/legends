package gg.warcraft.legends.app.combat.mage.effect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.Movement;
import gg.warcraft.monolith.api.core.Task;
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
import gg.warcraft.monolith.app.effect.renderer.SimpleEffectRenderer;

public class HailBlastEffect implements Runnable, Cancellable {
    private static final int ITERATIONS = 15;

    private final EffectVectorsFactory effectVectorsFactory;
    private final Movement movement;
    private final Particle particle;
    private final Effect effect;
    private final Task task;

    private int iteration;

    @Inject
    public HailBlastEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                           EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                           TimeUtils timeUtils, @Assisted Movement movement) {
        super(caster.getLocation().add(0, 1, 0).add(caster.getLocation().getDirection().multiply(1.5))); // TODO make VectorUtils with vectors for HEAD_OFFSET, CHEST_OFFSET, HEEL_OFFSET etc
        this.particle = particleFactory.createSimpleParticle(ParticleType.CLOUD);
        Duration duration = timeUtils.createDurationInTicks(ITERATIONS);
        this.iteration = 1;

        this.effect.cancel();
    }

    @Override
    public void run() {
        effect.clearRenderers();

        EffectVectors circleVectors = effectVectorsFactory.createCircleVectors(0.4f + 0.2f * iteration, 4 * iteration);
        circleVectors.rotateAroundAxisX(Math.toRadians(90 + getLocation().getPitch()));
        circleVectors.rotateAroundAxisY(Math.toRadians(-getLocation().getYaw()));
        EffectRenderer renderer = new SimpleEffectRenderer(particle, circleVectors);
        effect.addRenderer(renderer);

        effect.run();
        iteration += 1;
        getLocation().add(getLocation().getDirection());
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