package gg.warcraft.legends.objective.effect;

import de.slikey.effectlib.util.ParticleEffect;
import gg.warcraft.monolith.effect.EffectRenderer;
import gg.warcraft.monolith.effect.EffectVectors;
import gg.warcraft.monolith.effect.MonolithParticle;
import gg.warcraft.monolith.effect.SimpleMonolithEffect;
import gg.warcraft.monolith.effect.particle.SimpleMonolithParticle;
import gg.warcraft.monolith.effect.renderer.IterativeEffectRenderer;
import gg.warcraft.monolith.effect.vectors.RingVectors;
import gg.warcraft.monolith.entity.MonolithEntity;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.function.Supplier;

public class ObjectivePortalEffect extends SimpleMonolithEffect {
    private static final double RADIUS = 0.5;
    private static final int FRAMES = 20;
    private static final Vector OFFSET = new Vector(0.0, 0.05, 0.0);

    private final Supplier<Color> colorSupplier;
    private Color currentColor;

    public ObjectivePortalEffect(Location target, Supplier<Color> colorSupplier) {
        super(() -> target.clone().add(OFFSET));
        this.colorSupplier = colorSupplier;
        this.currentColor = colorSupplier.get();
    }

    public void setColor(Color color) {
        currentColor = color;
        clearRenderers();
        {
            MonolithParticle particle = new SimpleMonolithParticle(ParticleEffect.REDSTONE, color);
            EffectVectors vectors = new RingVectors(RADIUS, FRAMES);
            EffectRenderer renderer = new IterativeEffectRenderer(particle, vectors);
            addRenderer(renderer);
        }
        {
            MonolithParticle particle = new SimpleMonolithParticle(ParticleEffect.REDSTONE, color);
            EffectVectors vectors = new RingVectors(RADIUS, FRAMES);
            vectors.forEach(vector -> vector.setY(vector.getY() + 0.45));
            EffectRenderer renderer = new IterativeEffectRenderer(particle, vectors);
            addRenderer(renderer);
        }
        {
            MonolithParticle particle = new SimpleMonolithParticle(ParticleEffect.REDSTONE, color);
            EffectVectors vectors = new RingVectors(RADIUS, FRAMES);
            vectors.forEach(vector -> vector.setY(vector.getY() + 0.9));
            EffectRenderer renderer = new IterativeEffectRenderer(particle, vectors);
            addRenderer(renderer);
        }
    }

    @Override
    public void run() {
        Color color = colorSupplier.get();
        if (currentColor != color) {
            setColor(color);
        }
        super.run();
    }
}
