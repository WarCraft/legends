package gg.warcraft.legends.app.combat.passive;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.core.Task;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.event.EntityDeathEvent;
import gg.warcraft.monolith.api.entity.event.EntityPreFatalDamageEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.UUID;

public class HumanWarriorPassive extends AbstractRacialPassive {
    private static final String NAME = "Last Stand";
    private static final int DURATION_IN_SECONDS = 2;
    private static final long COOLDOWN_IN_MILLIS = 300 * TimeUtils.MILLIS_PER_SECOND;

    private final EntityCommandService entityCommandService;
    private final TaskService taskService;
    private final Duration duration;

    private Task task;
    private long cooldown;

    @Inject
    public HumanWarriorPassive(EntityCommandService entityCommandService, TaskService taskService, TimeUtils timeUtils,
                               @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.entityCommandService = entityCommandService;
        this.taskService = taskService;
        this.duration = timeUtils.createDurationInSeconds(DURATION_IN_SECONDS);
    }

    @Override
    public void onEntityPreFatalDamageEvent(EntityPreFatalDamageEvent event) {
        if (task != null) {
            event.cancel();
        } else if (System.currentTimeMillis() >= cooldown) {
            cooldown = System.currentTimeMillis() + COOLDOWN_IN_MILLIS;
            task = taskService.runLater(() -> {
                task = null;
                entityCommandService.kill(event.getEntityId());
            }, duration);
            event.cancel();
        }
    }

    @Override
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
