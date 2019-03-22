package gg.warcraft.legends.app.combat.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.combat.service.WhoDamagedWhoRepository;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.event.EntityDeathEvent;

import java.util.UUID;

public class WhoDamagedWhoHandler {
    private final WhoDamagedWhoRepository whoDamagedWhoRepository;
    private final TaskService taskService;

    @Inject
    public WhoDamagedWhoHandler(WhoDamagedWhoRepository whoDamagedWhoRepository, TaskService taskService) {
        this.whoDamagedWhoRepository = whoDamagedWhoRepository;
        this.taskService = taskService;
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
        taskService.runNextTick(() -> whoDamagedWhoRepository.resetDamagers(event.getEntityId()));
    }
}
