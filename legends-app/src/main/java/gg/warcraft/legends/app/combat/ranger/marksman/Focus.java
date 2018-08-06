package gg.warcraft.legends.app.combat.ranger.marksman;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.legends.app.combat.ranger.effect.RangerEffectFactory;
import gg.warcraft.legends.app.combat.ranger.status.RangerStatusEffectFactory;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.UUID;

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
                 @Assisted("target") Entity meleeTarget) {
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
