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
import gg.warcraft.monolith.api.world.Location;

import java.util.function.Supplier;

public class FireballEffect implements Cancellable {
    private static final float RADIUS = 0.2f;
    private static final int PARTICLE_COUNT = 20;

    private final Effect effect;

    @Inject
    public FireballEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                          EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                          @Assisted Supplier<Location> location) {
        this.effect = effectFactory.createEffect(location);

        Particle innerParticle = particleFactory.createSimpleParticle(ParticleType.FLAME);
        EffectVectors innerVectors = effectVectorsFactory.createOriginVector();
        EffectRenderer innerRenderer = effectRendererFactory.createSimpleEffectRenderer(innerParticle, innerVectors);
        this.effect.addRenderer(innerRenderer);

        Particle outerParticle = particleFactory.createColorParticle(ParticleType.REDSTONE, ColorHue.ORANGE);
        EffectVectors outerVectors = effectVectorsFactory.createSphereVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer outerRenderer = effectRendererFactory.createSimpleEffectRenderer(outerParticle, outerVectors);
        this.effect.addRenderer(outerRenderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
