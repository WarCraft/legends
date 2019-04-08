package gg.warcraft.legends.app.combat.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.monolith.api.combat.event.ProjectilePreHitEvent;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;

import javax.inject.Inject;
import java.util.UUID;

public class ArrowFriendlyFireHandler {
    private final EntityQueryService entityQueryService;

    @Inject
    public ArrowFriendlyFireHandler(EntityQueryService entityQueryService) {
        this.entityQueryService = entityQueryService;
    }

    @Subscribe
    public void onProjectilePreHitEvent(ProjectilePreHitEvent event) {
        UUID shooterId = event.getShooterId();
        if (shooterId == null) {
            System.out.println("DEBUG shooterId was null in ArrowFriendlyFireHandler");
            return;
        }

        UUID entityId = event.getEntityId();
        if (entityId == null) {
            System.out.println("DEBUG entityId was null in ArrowFriendlyFireHandler");
            return;
        }

        Entity shooter = entityQueryService.getEntity(shooterId);
        Entity hitEntity = entityQueryService.getEntity(entityId);
        if (shooter.getTeam() == hitEntity.getTeam()) {
            event.cancel();
        }
    }
}
