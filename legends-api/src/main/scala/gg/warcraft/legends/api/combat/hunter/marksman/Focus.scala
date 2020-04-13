package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.entity.Entity

object Focus {
  class Factory extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Focus(caster, snapshot)
  }
}

class Focus(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
) extends MeleeAbility {}

/*
public class Focus extends AbstractAbility implements MeleeAbility, Cancellable {
    private final EntityCommandService entityCommandService;
    private final StatusCommandService statusCommandService;
    private final TimeUtils timeUtils;
    private final StatusEffect statusEffect;
    private final Cancellable effect;

    @Inject
    public Focus(EntityCommandService entityCommandService, StatusCommandService statusCommandService,
                 PlayerCommandService playerCommandService, RangerStatusEffectFactory rangerStatusEffectFactory,
                 RangerEffectFactory rangerEffectFactory, FocusRepository focusRepository, TimeUtils timeUtils,
                 @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                 @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.statusCommandService = statusCommandService;
        this.timeUtils = timeUtils;

        UUID casterId = caster.getId();
        Focus focus = focusRepository.getFocus(casterId);
        if (focus != null) {
            statusEffect = null;
            effect = null;

            focus.cancel();
            focusRepository.delete(focus);
        } else {
            Duration duration = timeUtils.createDurationInTicks(Integer.MAX_VALUE);
            entityCommandService.freeze(casterId, duration);

            float damageModifier = snapshot.getFloat("damage-modifier");
            this.statusEffect = rangerStatusEffectFactory.createFocusStatusEffect(casterId, damageModifier);
            statusCommandService.addStatusEffect(statusEffect, casterId);

            this.effect = rangerEffectFactory.createFocusEffect(caster);

            focusRepository.save(this);

            playerCommandService.sendNotification(casterId,
                    "You're currently focussing and can not move, cast [SHIFT LEFT CLICK] again to cancel.");
        }
    }

    @Override
    public void cancel() {
        entityCommandService.freeze(getCasterId(), timeUtils.immediately());
        statusCommandService.removeStatusEffect(statusEffect, getCasterId());
        effect.cancel();
    }

    @Override
    public void onHit(Entity entity) {
        // do nothing
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}
 */

/*

public class FocusEffect implements Cancellable {
    private static final float RADIUS = 0.5f;
    private static final int FRAMES = 20;
    private static final Vector3f OFFSET = new Vector3f(0, 0.1f, 0);

    private final Effect effect;

    @Inject
    public FocusEffect(EntityQueryService entityQueryService, ParticleFactory particleFactory,
                       EffectVectorsFactory effectVectorsFactory, EffectRendererFactory effectRendererFactory,
                       EffectFactory effectFactory, @Assisted Entity target) {
        EntityLocationSupplier entityLocationSupplier = new EntityLocationSupplier(entityQueryService, target);
        Supplier<Location> locationSupplier = () -> entityLocationSupplier.get().add(OFFSET);
        this.effect = effectFactory.createSimpleEffect(locationSupplier);

        Particle particle = particleFactory.createSimpleParticle(ParticleType.REDSTONE);
        EffectVectors vectors = effectVectorsFactory.createRingVectors(RADIUS, FRAMES);
        EffectRenderer renderer = effectRendererFactory.createIterativeEffectRenderer(particle, vectors);
        effect.addRenderer(renderer);
    }

    @Override
    public void cancel() {
        effect.cancel();
    }
}
 */

/*
public class FocusStatusEffect extends AbstractStatusEffect implements CombatStatusEffect {
    private static final String NAME = "Focus";
    private static final String TYPE = "legends.marksman.focus";

    private final CombatValueModifier damageModifier;

    @Inject
    public FocusStatusEffect(CombatFactory combatFactory, @Assisted UUID entityId,
                             @Assisted float damageModifier) {
        super(entityId, NAME, TYPE, null);
        this.damageModifier =
                combatFactory.createCombatValueModifier(CombatValueModifierType.PERCENT, damageModifier, this);
    }

    @Override
    public void onAbilityPreCastEvent(AbilityPreCastEvent event) {
        event.addDamageModifier(damageModifier);
    }
}
 */
