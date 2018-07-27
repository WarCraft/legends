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

public class ArcaneMissileEffect implements Cancellable {
    private static final float RADIUS = 0.1f;
    private static final int INNER_PARTICLE_COUNT = 1;
    private static final float INNER_PARTICLE_SPEED = 1;
    private static final int OUTER_PARTICLE_COUNT = 6;

    private final Effect effect;

    @Inject
    public ArcaneMissileEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                               EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                               @Assisted Movement movement) {
        this.effect = effectFactory.createSimpleEffect(movement);

        Particle innerParticle = particleFactory.createSpeedParticle(ParticleType.ENCHANTMENT_TABLE,
                INNER_PARTICLE_SPEED, INNER_PARTICLE_COUNT);
        EffectVectors innerVectors = effectVectorsFactory.createOriginVector();
        EffectRenderer innerRenderer = effectRendererFactory.createSimpleEffectRenderer(innerParticle, innerVectors);
        this.effect.addRenderer(innerRenderer);

        Particle outerParticle = particleFactory.createSimpleParticle(ParticleType.DRAGON_BREATH);
        EffectVectors outerVectors = effectVectorsFactory.createSphereVectors(RADIUS, OUTER_PARTICLE_COUNT);
        EffectRenderer outerRenderer = effectRendererFactory.createSimpleEffectRenderer(outerParticle, outerVectors);
        this.effect.addRenderer(outerRenderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
