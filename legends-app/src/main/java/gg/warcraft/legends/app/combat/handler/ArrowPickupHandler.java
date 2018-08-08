package gg.warcraft.legends.app.combat.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.monolith.api.combat.ProjectileType;
import gg.warcraft.monolith.api.combat.event.ProjectilePrePickupEvent;

public class ArrowPickupHandler {

    @Subscribe
    public void onProjectilePrePickupEvent(ProjectilePrePickupEvent event) {
        if (event.getProjectileType() == ProjectileType.ARROW) {
            event.cancel();
        }
    }
}
