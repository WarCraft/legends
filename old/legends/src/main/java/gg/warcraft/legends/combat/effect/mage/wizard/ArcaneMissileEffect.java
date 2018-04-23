package gg.warcraft.legends.combat.effect.mage.wizard;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.renderer.SpeedEffectRenderer;
import gg.warcraft.monolith.effect.vectors.OriginVector;
import gg.warcraft.monolith.effect.vectors.SphereVectors;
import org.bukkit.Location;

import java.util.function.Supplier;

public class ArcaneMissileEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 0.1;
    private static final int PARTICLE_COUNT = 6;
    private static final float PARTICLE_SPEED = 1f;

    public ArcaneMissileEffect(Supplier<Location> location) {
        super(location);

        MonolithParticle innerParticle = new SimpleMonolithParticle(ParticleEffect.ENCHANTMENT_TABLE);
        EffectVectors innerVectors = new OriginVector();
        EffectRenderer innerRenderer = new SpeedEffectRenderer(innerParticle, innerVectors, PARTICLE_SPEED);
        addRenderer(innerRenderer);

        MonolithParticle outerParticle = new SimpleMonolithParticle(ParticleEffect.DRAGON_BREATH);
        EffectVectors outerVectors = new SphereVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer outerRenderer = new SimpleEffectRenderer(outerParticle, outerVectors);
        addRenderer(outerRenderer);
    }
}
