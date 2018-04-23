package gg.warcraft.legends.combat.effect.mage.archon;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.MultiMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.CircleVectors;
import gg.warcraft.monolith.effect.vectors.DomeVectors;
import gg.warcraft.monolith.util.Duration;
import org.bukkit.Location;

import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class SupernovaImplosionEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 7;
    private static final int PARTICLE_COUNT = 200;
    private static final int ITERATIONS = 5;

    private double radius;
    private int particleCount;

    public SupernovaImplosionEffect(Location location) {
        super(() -> location);
        cancel(new Duration(ITERATIONS, TICKS));
        radius = RADIUS;
        particleCount = PARTICLE_COUNT;
    }

    @Override
    public void run() {
        clearRenderers();
        MonolithParticle particle = new MultiMonolithParticle(ParticleEffect.SMOKE_NORMAL, ParticleEffect.SPELL_WITCH);

        EffectVectors circleVectors = new CircleVectors(radius, particleCount);
        EffectRenderer circleRenderer = new SimpleEffectRenderer(particle, circleVectors);
        addRenderer(circleRenderer);

        EffectVectors domeVectors = new DomeVectors(radius, particleCount);
        EffectRenderer domeRenderer = new SimpleEffectRenderer(particle, domeVectors);
        addRenderer(domeRenderer);

        super.run();
        radius *= 0.75;
        particleCount *= 0.75;
    }
}
