package gg.warcraft.legends.app.combat.mage.effect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
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
import gg.warcraft.monolith.api.util.ColorHue;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Location;

public class MoltenStormCircleEffect implements Cancellable {
    private static final float RADIUS = 7.5f;
    private static final int PARTICLE_COUNT = 100;

    private final Effect effect;

    @Inject
    public MoltenStormCircleEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                                   EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                                   @Assisted Location location, @Assisted Duration duration) {
        Location adjustedLocation = location.add(0, 0.15f, 0);
        this.effect = effectFactory.createSimpleEffect(() -> adjustedLocation);
        this.effect.cancel(duration);

        Particle particle = particleFactory.createColorParticle(ParticleType.REDSTONE, ColorHue.ORANGE);
        EffectVectors vectors = effectVectorsFactory.createRingVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, vectors);
        this.effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
