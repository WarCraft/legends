package gg.warcraft.legends.app.combat.warrior.effect;

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
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.Location;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Supplier;

public class GrudgeEffect implements Cancellable {
    private static final Vector3fc OFFSET = new Vector3f(0, 1.5f, 0);
    private static final int PERIOD_IN_TICKS = 5;

    private final Effect effect;

    @Inject
    public GrudgeEffect(EntityQueryService entityQueryService, ParticleFactory particleFactory,
                        EffectVectorsFactory effectVectorsFactory, EffectRendererFactory effectRendererFactory,
                        EffectFactory effectFactory, TimeUtils timeUtils, @Assisted Entity target,
                        @Assisted Duration duration) {
        Supplier<Location> locationSupplier = () -> {
            Entity entity = entityQueryService.getEntity(target.getId());
            return entity.getLocation().add(OFFSET);
        };
        Duration period = timeUtils.createDurationInTicks(PERIOD_IN_TICKS);
        this.effect = effectFactory.createPeriodicEffect(locationSupplier, period);
        this.effect.cancel(duration);

        Particle particle = particleFactory.createSimpleParticle(ParticleType.VILLAGER_ANGRY);
        EffectVectors vectors = effectVectorsFactory.createOriginVector();
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, vectors);
        this.effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
