package gg.warcraft.legends.siege.effect.trebuchet;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.RangedMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.OriginVector;
import gg.warcraft.monolith.effect.vectors.SphereVectors;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.function.Supplier;

public class TrebuchetProjectileEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 0.5;
    private static final int PARTICLE_COUNT = 100;
    private static final double RANGE = 150;

    public TrebuchetProjectileEffect(Supplier<Location> location) {
        super(location);

        MonolithParticle innerParticle = new RangedMonolithParticle(ParticleEffect.FLAME, RANGE);
        EffectVectors innerVectors = new OriginVector();
        EffectRenderer innerRenderer = new SimpleEffectRenderer(innerParticle, innerVectors);
        addRenderer(innerRenderer);

        MonolithParticle outerParticle = new RangedMonolithParticle(ParticleEffect.REDSTONE, Color.ORANGE, RANGE);
        EffectVectors outerVectors = new SphereVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer outerRenderer = new SimpleEffectRenderer(outerParticle, outerVectors);
        addRenderer(outerRenderer);
    }
}
