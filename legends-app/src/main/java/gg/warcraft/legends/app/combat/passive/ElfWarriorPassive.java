package gg.warcraft.legends.app.combat.passive;

import gg.warcraft.combat.api.ability.event.AbilityPreHitEvent;
import gg.warcraft.combat.api.status.CombatStatusEffect;

import java.util.UUID;

public class ElfWarriorPassive extends AbstractRacialPassive implements CombatStatusEffect {
    private static final String NAME = "Evasion";
    private static final int MAX_COUNT = 7;

    private int count;

    public ElfWarriorPassive(UUID entityId) {
        super(entityId, NAME, null);
    }

    @Override
    public void onAbilityPreHitEvent(AbilityPreHitEvent event) {
        if (event.getTargetId().equals(getEntityId())) {
            count += 1;
            if (count >= MAX_COUNT) {
                event.cancel();
                count = 0;
            }
        }
    }
}
