package gg.warcraft.legends.combat.passive;

import gg.warcraft.monolith.combat.event.MonolithEntityHitEvent;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;

public class ElfWarriorPassive extends AbstractStatusEffect {
    private static final int MAX_COUNT = 7;

    private final MonolithEntity entity;

    private int count;

    public ElfWarriorPassive(MonolithEntity entity) {
        this.entity = entity;
        this.count = 0;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void onHitEvent(MonolithEntityHitEvent event) {
        System.out.println("MonolithEntityHitEvent for " + this.getClass().getSimpleName());
        count += 1;
        if (count >= MAX_COUNT) {
            event.setCancelled(true);
            count = 0;

            // TODO remove
            event.getEntity().sendMessage("[ElfWarriorPassive]: dodged");
        } else {
            // TODO remove
            event.getEntity().sendMessage("[ElfWarriorPassive]: current count: " + count);
        }
    }
}
