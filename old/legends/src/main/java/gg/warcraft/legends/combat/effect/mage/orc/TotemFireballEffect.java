package gg.warcraft.legends.combat.effect.mage.orc;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.OriginVector;
import gg.warcraft.monolith.effect.vectors.SphereVectors;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.function.Supplier;

public class TotemFireballEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 0.1;
    private static final int PARTICLE_COUNT = 8;

    public TotemFireballEffect(Supplier<Location> location) {
        super(location);

        MonolithParticle innerParticle = new SimpleMonolithParticle(ParticleEffect.FLAME);
        EffectVectors innerVectors = new OriginVector();
        EffectRenderer innerRenderer = new SimpleEffectRenderer(innerParticle, innerVectors);
        addRenderer(innerRenderer);

        MonolithParticle outerParticle = new SimpleMonolithParticle(ParticleEffect.REDSTONE, Color.ORANGE);
        EffectVectors outerVectors = new SphereVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer outerRenderer = new SimpleEffectRenderer(outerParticle, outerVectors);
        addRenderer(outerRenderer);
    }
}
