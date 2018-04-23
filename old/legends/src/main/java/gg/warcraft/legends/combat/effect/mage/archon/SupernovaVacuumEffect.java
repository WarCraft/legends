package gg.warcraft.legends.combat.effect.mage.archon;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.MultiMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.RingVectors;
import org.bukkit.Location;

public class SupernovaVacuumEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 7;
    private static final int PARTICLE_COUNT = 100;

    public SupernovaVacuumEffect(Location location) {
        super(() -> location);

        MonolithParticle particle = new MultiMonolithParticle(ParticleEffect.SMOKE_NORMAL, ParticleEffect.SPELL_WITCH);
        EffectVectors vectors = new RingVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = new SimpleEffectRenderer(particle, vectors);
        this.addRenderer(renderer);
    }
}
