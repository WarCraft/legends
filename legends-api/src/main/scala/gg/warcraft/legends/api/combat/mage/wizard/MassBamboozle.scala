package gg.warcraft.legends.api.combat.mage.wizard

class MassBamboozle {

}


/*
public class MassBamboozleCountdownEffect implements Cancellable {
    private static final float RADIUS = 5;
    private static final int PARTICLE_COUNT = 80;

    private final Effect effect;

    @Inject
    public MassBamboozleCountdownEffect(ParticleFactory particleFactory, EffectVectorsFactory effectVectorsFactory,
                                        EffectRendererFactory effectRendererFactory, EffectFactory effectFactory,
                                        @Assisted Location location) {
        Location adjustedLocation = location.add(0, 0.15f, 0);
        Supplier<Location> movement = () -> adjustedLocation;
        this.effect = effectFactory.createSimpleEffect(movement);

        Particle particle = particleFactory.createSimpleParticle(ParticleType.DRAGON_BREATH);
        EffectVectors vectors = effectVectorsFactory.createRingVectors(RADIUS, PARTICLE_COUNT);
        EffectRenderer renderer = effectRendererFactory.createSimpleEffectRenderer(particle, vectors);
        this.effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
 */
