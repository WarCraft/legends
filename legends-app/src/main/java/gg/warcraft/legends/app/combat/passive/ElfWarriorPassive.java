package gg.warcraft.legends.app.combat.passive;

import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.app.entity.status.AbstractStatusEffect;

import java.util.UUID;

public class ElfWarriorPassive extends AbstractStatusEffect implements StatusEffect {
    private static final int MAX_COUNT = 7;

    private int count;

    public ElfWarriorPassive(UUID entityid) {
        super(entityid, null);
        this.count = 0;
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
