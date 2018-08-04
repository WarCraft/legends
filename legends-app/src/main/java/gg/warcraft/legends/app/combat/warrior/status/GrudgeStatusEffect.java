package gg.warcraft.legends.app.combat.warrior.status;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.util.Duration;

import java.util.UUID;

public class GrudgeStatusEffect implements StatusEffect {
    private final EntityCommandService entityCommandService;
    private final UUID entityId;
    private final UUID casterId;
    private final Duration duration;
    private final CombatValue heal;

    @Inject
    public GrudgeStatusEffect(EntityCommandService entityCommandService,
                              @Assisted("entity") UUID entityId, @Assisted("caster") UUID casterId,
                              @Assisted Duration duration, @Assisted CombatValue heal) {
        this.entityCommandService = entityCommandService;
        this.entityId = entityId;
        this.casterId = casterId;
        this.duration = duration;
        this.heal = heal;
    }

    @Override
    public UUID getEntityId() {
        return entityId;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        UUID damagerId = event.getDamage().getSource().getEntityId();
        if (casterId.equals(damagerId)) {
            entityCommandService.heal(casterId, heal);
        }
    }
}
