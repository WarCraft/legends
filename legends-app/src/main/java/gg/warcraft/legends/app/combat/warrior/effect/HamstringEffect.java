package gg.warcraft.legends.app.combat.warrior.effect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.app.combat.EntityLocationSupplier;
import gg.warcraft.monolith.api.effect.Effect;
import gg.warcraft.monolith.api.effect.EffectFactory;
import gg.warcraft.monolith.api.effect.EffectRenderer;
import gg.warcraft.monolith.api.effect.EffectRendererFactory;
import gg.warcraft.monolith.api.effect.EffectVectors;
import gg.warcraft.monolith.api.effect.EffectVectorsFactory;
import gg.warcraft.monolith.api.effect.Particle;
import gg.warcraft.monolith.api.effect.ParticleFactory;
import gg.warcraft.monolith.api.effect.ParticleType;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.ColorHue;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Location;

import java.util.function.Supplier;

public class HamstringEffect implements Cancellable {
    private static final float RADIUS = 0.5f;
    private static final int FRAMES = 20;
    private static final Vector3f OFFSET = new Vector3f(0, 0.1f, 0); // TODO make VectorUtils with vectors for HEAD_OFFSET, CHEST_OFFSET, HEEL_OFFSET etc

    private final Effect effect;

    @Inject
    public HamstringEffect(EntityQueryService entityQueryService, ParticleFactory particleFactory,
                           EffectVectorsFactory effectVectorsFactory, EffectRendererFactory effectRendererFactory,
                           EffectFactory effectFactory, @Assisted Entity target, @Assisted Duration duration) {
        EntityLocationSupplier entityLocationSupplier = new EntityLocationSupplier(entityQueryService, target);
        Supplier<Location> locationSupplier = () -> entityLocationSupplier.get().add(OFFSET);
        this.effect = effectFactory.createSimpleEffect(locationSupplier);
        this.effect.cancel(duration);

        Particle particle = particleFactory.createColorParticle(ParticleType.SPELL_MOB, ColorHue.WHITE);
        EffectVectors vectors = effectVectorsFactory.createRingVectors(RADIUS, FRAMES);
        EffectRenderer renderer = effectRendererFactory.createIterativeEffectRenderer(particle, vectors);
        this.effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
