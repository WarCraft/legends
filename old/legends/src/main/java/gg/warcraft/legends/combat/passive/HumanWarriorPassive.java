package gg.warcraft.legends.combat.passive;

import gg.warcraft.monolith.combat.event.MonolithEntityDeathEvent;
import gg.warcraft.monolith.entity.AbstractStatusEffect;
import gg.warcraft.monolith.entity.MonolithEntity;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.ChatColor;

public class HumanWarriorPassive extends AbstractStatusEffect {
    private static final int DURATION = 2 * TimeUtils.TICKS_PER_SECOND;
    private static final long COOLDOWN = 300L * TimeUtils.MILLIS_PER_SECOND;

    private final MonolithEntity entity;

    private MonolithTask task;
    private long cooldown;

    public HumanWarriorPassive(MonolithEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void onDeathEvent(MonolithEntityDeathEvent event) {
        if (task != null) {
            event.setCancelled(true);
        } else if (System.currentTimeMillis() >= cooldown) {
            entity.sendMessage(ChatColor.YELLOW + "You are performing a last stand and are invulnerable for 2 seconds!");
            cooldown = System.currentTimeMillis() + COOLDOWN;
            task = RunnableUtils.runLater(() -> {
                // NOTE removed for now: event.getEntity().setHealth(0);
                task = null;
            }, DURATION);

            event.setCancelled(true);
        }
    }
}
