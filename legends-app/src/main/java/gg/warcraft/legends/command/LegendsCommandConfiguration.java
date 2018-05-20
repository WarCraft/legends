package gg.warcraft.legends.command;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.event.EventService;
import gg.warcraft.monolith.app.command.handler.CommandExecutedHandler;

public class LegendsCommandConfiguration {

    @Inject
    public LegendsCommandConfiguration(EventService eventService, CommandExecutedHandler commandExecutedHandler) {
        eventService.subscribe(commandExecutedHandler);
    }
}
