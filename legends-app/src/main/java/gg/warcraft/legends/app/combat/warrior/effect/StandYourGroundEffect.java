package gg.warcraft.legends.app.combat.warrior.effect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.core.TaskService;
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
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Location;

import java.util.function.Supplier;

public class StandYourGroundEffect implements Runnable, Cancellable {
    private static final float RADIUS = 2.5f;
    private static final int PARTICLE_COUNT = 50;

    private final EffectVectorsFactory effectVectorsFactory;
    private final EffectRendererFactory effectRendererFactory;
    private final Effect effect;
    private final Particle particle;

    @Inject
    public StandYourGroundEffect(EntityQueryService entityQueryService, TaskService taskService,
                                 ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                                 EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                                 @Assisted Entity target, @Assisted Duration duration) {
        this.effectVectorsFactory = effectVectorsFactory;
        this.effectRendererFactory = effectRendererFactory;

        Supplier<Location> locationSupplier = () -> {
            Entity entity = entityQueryService.getEntity(target.getId());
            return entity.getLocation();
        };
        this.effect = effectFactory.createDynamicEffect(locationSupplier, this);
        this.particle = particleFactory.createSimpleParticle(ParticleType.CRIT_MAGIC);

        taskService.runLater(effect::cancel, duration);
    }

    @Override
    public void run() {
        effect.clearRenderers();
        EffectVectors vectors = effectVectorsFactory.createDomeVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, vectors);
        effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
