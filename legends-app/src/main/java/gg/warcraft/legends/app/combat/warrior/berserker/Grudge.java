package gg.warcraft.legends.app.combat.warrior.berserker;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.legends.app.combat.warrior.status.GrudgeStatusEffect;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.util.Duration;

import java.util.UUID;

public class Grudge extends AbstractAbility implements MeleeAbility {
    private final StatusCommandService statusCommandService;
    private final AbilitySnapshot snapshot;
    private final UUID casterId;

    @Inject
    public Grudge(StatusCommandService statusCommandService, @Assisted AbilitySnapshot snapshot,
                  @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.statusCommandService = statusCommandService;
        this.snapshot = snapshot;
        this.casterId = caster.getId();
    }

    @Override
    public void onHit(Entity entity) {
        double heal = snapshot.getFloat(AbilityConfigurationValue.HEAL);
        Duration duration = snapshot.getDuration();

        StatusEffect grudge = new GrudgeStatusEffect(casterId, heal);
        statusCommandService.addStatusEffect(grudge, entity.getId());

        // TODO do we want to allow addStatusEffect to already take a duration? Should it be on the status effect itself?
        RunnableUtils.runLater(() -> target.removeStatusEffect(grudge), duration.as(Duration.Type.TICKS));

        // TODO new GrudgeEffect(target, duration);
        // TODO play hit sound
    }

    @Override
    public void onMiss(Entity entity) {
        // TODO play miss sound
    }
}
