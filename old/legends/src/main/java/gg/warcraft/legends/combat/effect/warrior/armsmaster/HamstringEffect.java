package gg.warcraft.legends.combat.effect.warrior.armsmaster;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.IterativeEffectRenderer;
import gg.warcraft.monolith.effect.vectors.RingVectors;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;
import org.bukkit.Color;
import org.bukkit.util.Vector;

public class HamstringEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 0.5;
    private static final int FRAMES = 20;
    private static final Vector OFFSET = new Vector(0.0, 0.1, 0.0);

    public HamstringEffect(MonolithEntity target, Duration duration) {
        super(() -> target.getLocation().add(OFFSET));
        cancel(duration);

        MonolithParticle particle = new SimpleMonolithParticle(ParticleEffect.SPELL_MOB, Color.WHITE);
        EffectVectors vectors = new RingVectors(RADIUS, FRAMES);
        EffectRenderer renderer = new IterativeEffectRenderer(particle, vectors);
        addRenderer(renderer);
    }
}
