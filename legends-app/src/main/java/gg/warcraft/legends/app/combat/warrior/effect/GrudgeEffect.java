package gg.warcraft.legends.app.combat.warrior.effect;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.OriginVector;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;
import org.bukkit.util.Vector;

public class GrudgeEffect extends SimpleMonolithEffect {
    private static final Vector OFFSET = new Vector(0.0, 1.5, 0.0);
    private static final Duration PERIOD = new Duration(5, Duration.Type.TICKS);

    public GrudgeEffect(MonolithEntity target, Duration duration) {
        super(() -> target.getLocation().add(OFFSET), PERIOD);
        cancel(duration);

        MonolithParticle particle = new SimpleMonolithParticle(ParticleEffect.VILLAGER_ANGRY);
        EffectVectors vectors = new OriginVector();
        EffectRenderer renderer = new SimpleEffectRenderer(particle, vectors);
        addRenderer(renderer);
    }
}
