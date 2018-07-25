package gg.warcraft.legends.app.combat.warrior.effect;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.DomeVectors;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;

public class StandYourGroundEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 2.5;
    private static final int PARTICLE_COUNT = 50;

    private final MonolithEntity caster;
    private final MonolithParticle particle;

    public StandYourGroundEffect(MonolithEntity caster, Duration duration) {
        super(caster::getLocation);
        this.caster = caster;
        particle = new SimpleMonolithParticle(ParticleEffect.CRIT_MAGIC);
        cancel(duration);
    }

    @Override
    public void run() {
        EffectVectors vectors = new DomeVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = new SimpleEffectRenderer(particle, vectors);
        renderer.renderAt(caster.getLocation());
    }
}
