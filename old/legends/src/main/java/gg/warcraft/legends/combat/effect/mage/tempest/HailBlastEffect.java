package gg.warcraft.legends.combat.effect.mage.tempest;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.SimpleEffectRenderer;
import gg.warcraft.monolith.effect.vectors.CircleVectors;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.Duration;

import static gg.warcraft.monolith.util.Duration.Type.TICKS;

public class HailBlastEffect extends SimpleMonolithEffect {
    private static final int ITERATIONS = 15;

    private final MonolithParticle particle;

    private int iteration;

    public HailBlastEffect(MonolithEntity caster) {
        super(caster.getLocation().add(0, 1, 0).add(caster.getLocation().getDirection().multiply(1.5))); // TODO make VectorUtils with vectors for HEAD_OFFSET, CHEST_OFFSET, HEEL_OFFSET etc
        particle = new SimpleMonolithParticle(ParticleEffect.CLOUD);
        iteration = 1;
        cancel(new Duration(ITERATIONS, TICKS));
    }

    @Override
    public void run() {
        clearRenderers();

        EffectVectors circleVectors = new CircleVectors(0.4 + 0.2 * iteration, 4 * iteration);
        circleVectors.rotateVectorsAroundAxisX(Math.toRadians(90 + getLocation().getPitch()));
        circleVectors.rotateVectorsAroundAxisY(Math.toRadians(-getLocation().getYaw()));
        EffectRenderer renderer = new SimpleEffectRenderer(particle, circleVectors);
        addRenderer(renderer);

        super.run();
        iteration += 1;
        getLocation().add(getLocation().getDirection());
    }
}
