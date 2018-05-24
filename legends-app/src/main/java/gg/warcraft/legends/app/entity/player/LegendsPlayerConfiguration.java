package gg.warcraft.legends.app.entity.player;

import com.google.inject.Inject;
import gg.warcraft.legends.app.entity.player.handler.PlayerDataInitializationHandler;
import gg.warcraft.monolith.api.event.EventService;

public class LegendsPlayerConfiguration {

    @Inject
    public LegendsPlayerConfiguration(EventService eventService,
                                      PlayerDataInitializationHandler playerDataInitializationHandler) {
        eventService.subscribe(playerDataInitializationHandler);
    }
}
