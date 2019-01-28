package gg.warcraft.legends.app.combat.ranger.effect;

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
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.world.location.Location;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Supplier;

public class FocusEffect implements Cancellable {
    private static final float RADIUS = 0.5f;
    private static final int FRAMES = 20;
    private static final Vector3fc OFFSET = new Vector3f(0, 0.1f, 0);

    private final Effect effect;

    @Inject
    public FocusEffect(EntityQueryService entityQueryService, ParticleFactory particleFactory,
                       EffectVectorsFactory effectVectorsFactory, EffectRendererFactory effectRendererFactory,
                       EffectFactory effectFactory, @Assisted Entity target) {
        EntityLocationSupplier entityLocationSupplier = new EntityLocationSupplier(entityQueryService, target);
        Supplier<Location> locationSupplier = () -> entityLocationSupplier.get().add(OFFSET);
        this.effect = effectFactory.createSimpleEffect(locationSupplier);

        Particle particle = particleFactory.createSimpleParticle(ParticleType.REDSTONE);
        EffectVectors vectors = effectVectorsFactory.createRingVectors(RADIUS, FRAMES);
        EffectRenderer renderer = effectRendererFactory.createIterativeEffectRenderer(particle, vectors);
        effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
