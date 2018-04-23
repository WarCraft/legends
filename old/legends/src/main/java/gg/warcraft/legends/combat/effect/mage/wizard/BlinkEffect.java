package gg.warcraft.legends.combat.effect.mage.wizard;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.RingVectors;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.movement.InterpolatedMovement;
import gg.warcraft.monolith.util.movement.Movement;
import org.bukkit.Location;

import java.util.function.Supplier;

public class BlinkEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 1;
    private static final int PARTICLE_COUNT = 10;
    private static final int INTERPOLATION_COUNT = 8;

    private final MonolithParticle particle;

    public BlinkEffect(MonolithEntity caster) {
        super(getMovementAsLocationSupplier(caster.getLocation()));
        particle = new SimpleMonolithParticle(ParticleEffect.SPELL_WITCH);

    }

    private static Supplier<Location> getMovementAsLocationSupplier(Location location) {
        Movement movement = new InterpolatedMovement(location.clone().add(0, 1, 0), location, INTERPOLATION_COUNT);
        return movement::nextLocation;
    }

    @Override
    public void run() {
        clearRenderers(); // FIXME why is this overriding and not just in constructor?
        EffectVectors vectors = new RingVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = new SimpleEffectRenderer(particle, vectors);
        addRenderer(renderer);
        super.run();
    }
}
