package gg.warcraft.legends.app.combat.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.combat.service.WhoDamagedWhoRepository;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.event.EntityDeathEvent;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.UUID;

public class WhoDamagedWhoHandler {
    private final WhoDamagedWhoRepository whoDamagedWhoRepository;
    private final TaskService taskService;
    private final TimeUtils timeUtils;

    @Inject
    public WhoDamagedWhoHandler(WhoDamagedWhoRepository whoDamagedWhoRepository,
                                TaskService taskService,
                                TimeUtils timeUtils) {
        this.whoDamagedWhoRepository = whoDamagedWhoRepository;
        this.taskService = taskService;
        this.timeUtils = timeUtils;
    }

    @Subscribe
    public void onEntityDamageEvent(EntityDamageEvent event) {
        UUID damagerId = event.getDamage().getSource().getEntityId();
        if (damagerId != null) {
            whoDamagedWhoRepository.addDamager(damagerId, event.getEntityId());
        }
    }

    @Subscribe
    public void onEntityDeathEvent(EntityDeathEvent event) {
        taskService.runLater(() -> whoDamagedWhoRepository.resetDamagers(event.getEntityId()),
                timeUtils.createDurationInTicks(2));
    }
}
