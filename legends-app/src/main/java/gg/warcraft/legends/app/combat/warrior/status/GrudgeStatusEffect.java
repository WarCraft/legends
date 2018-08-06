package gg.warcraft.legends.app.combat.warrior.status;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.app.entity.status.AbstractStatusEffect;

import java.util.UUID;

public class GrudgeStatusEffect extends AbstractStatusEffect {
    private final EntityCommandService entityCommandService;
    private final UUID casterId;
    private final CombatValue heal;

    @Inject
    public GrudgeStatusEffect(EntityCommandService entityCommandService,
                              @Assisted("entity") UUID entityId, @Assisted("caster") UUID casterId,
                              @Assisted Duration duration, @Assisted CombatValue heal) {
        super(entityId, duration);
        this.entityCommandService = entityCommandService;
        this.casterId = casterId;
        this.heal = heal;
    }

    @Override
    public String getType() {
        return "legends.berzerker.grudge";
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        UUID damagerId = event.getDamage().getSource().getEntityId();
        if (casterId.equals(damagerId)) {
            entityCommandService.heal(casterId, heal);
        }
    }
}
