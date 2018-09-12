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
import gg.warcraft.monolith.api.world.location.Location;

import java.util.function.Supplier;

public class MassBamboozleCountdownEffect implements Cancellable {
    private static final float RADIUS = 5;
    private static final int PARTICLE_COUNT = 80;

    private final Effect effect;

    @Inject
    public MassBamboozleCountdownEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                                        EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                                        @Assisted Location location) {
        Location adjustedLocation = location.add(0, 0.15f, 0);
        Supplier<Location> movement = () -> adjustedLocation;
        this.effect = effectFactory.createSimpleEffect(movement);

        Particle particle = particleFactory.createSimpleParticle(ParticleType.DRAGON_BREATH);
        EffectVectors vectors = effectVectorsFactory.createRingVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, vectors);
        this.effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
