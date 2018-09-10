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

public class HailBlastEffect implements Runnable, Cancellable {
    private static final int ITERATIONS = 15;

    private final EffectVectorsFactory effectVectorsFactory;
    private final EffectRendererFactory effectRendererFactory;
    private final float pitch;
    private final float yaw;
    private final Particle particle;
    private final Effect effect;

    private int iteration;

    @Inject
    public HailBlastEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                           EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                           TimeUtils timeUtils, @Assisted Movement movement, @Assisted("pitch") float pitch,
                           @Assisted("yaw") float yaw) {
        this.effectVectorsFactory = effectVectorsFactory;
        this.effectRendererFactory = effectRendererFactory;
        this.pitch = pitch;
        this.yaw = yaw;

        this.effect = effectFactory.createDynamicEffect(movement, this);
        Duration duration = timeUtils.createDurationInTicks(ITERATIONS);
        this.effect.cancel(duration);

        this.particle = particleFactory.createSimpleParticle(ParticleType.CLOUD);
        this.iteration = 1;
    }

    @Override
    public void run() {
        effect.clearRenderers();

        EffectVectors circleVectors = effectVectorsFactory.createCircleVectors(0.4f + 0.2f * iteration, 4 * iteration);
        circleVectors = circleVectors.rotateAroundAxisX((float) Math.toRadians(90 + pitch));
        circleVectors = circleVectors.rotateAroundAxisY((float) Math.toRadians(-yaw));
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, circleVectors);
        effect.addRenderer(renderer);

        iteration += 1;
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
