package gg.warcraft.legends.combat.effect.mage.archon;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.MultiMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.SphereVectors;
import org.bukkit.Location;

import java.util.function.Supplier;

public class SupernovaProjectileEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 1;
    private static final int PARTICLE_COUNT = 25;

    public SupernovaProjectileEffect(Supplier<Location> location) {
        super(location);
        MonolithParticle particle = new MultiMonolithParticle(ParticleEffect.SMOKE_NORMAL, ParticleEffect.SPELL_WITCH);
        EffectVectors vectors = new SphereVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = new SimpleEffectRenderer(particle, vectors);
        this.addRenderer(renderer);
    }
}
