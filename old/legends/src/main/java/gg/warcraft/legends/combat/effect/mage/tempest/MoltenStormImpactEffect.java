package gg.warcraft.legends.combat.effect.mage.tempest;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.RingVectors;
import org.bukkit.Color;
import org.bukkit.Location;

public class MoltenStormImpactEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 7.5;
    private static final int PARTICLE_COUNT = 100;

    public MoltenStormImpactEffect(Location location) {
        super(() -> location.clone().add(0, 0.15, 0));

        MonolithParticle particle = new SimpleMonolithParticle(ParticleEffect.REDSTONE, Color.ORANGE);
        EffectVectors vectors = new RingVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = new SimpleEffectRenderer(particle, vectors);
        this.addRenderer(renderer);
    }
}
