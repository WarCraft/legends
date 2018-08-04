package gg.warcraft.legends.app.entity.player.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.app.entity.player.event.SimplePlayerFavorGainedEvent;
import gg.warcraft.legends.app.entity.player.event.SimplePlayerFavorLostEvent;
import gg.warcraft.legends.app.entity.player.event.SimplePlayerGoldGainedEvent;
import gg.warcraft.legends.app.entity.player.event.SimplePlayerGoldLostEvent;
import gg.warcraft.monolith.api.core.Event;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.entity.player.event.PlayerCurrencyGainedEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerCurrencyLostEvent;

public class PlayerCurrencyEventHandler {
    private final EventService eventService;

    @Inject
    public PlayerCurrencyEventHandler(EventService eventService) {
        this.eventService = eventService;
    }

    @Subscribe
    public void onPlayerCurrencyGainedEvent(PlayerCurrencyGainedEvent event) {
        switch (event.getCurrency()) {
            case "Favor":
                Event playerFavorGainedEvent = new SimplePlayerFavorGainedEvent(event.getPlayerId(), event.getAmount(),
                        event.getNewCurrent(), event.getNewLifetime());
                eventService.publish(playerFavorGainedEvent);
                break;
            case "Gold":
                Event playerGoldGainedEvent = new SimplePlayerGoldGainedEvent(event.getPlayerId(), event.getAmount(),
                        event.getNewCurrent(), event.getNewLifetime());
                eventService.publish(playerGoldGainedEvent);
                break;
        }
    }

    @Subscribe
    public void onPlayerCurrencyLostEvent(PlayerCurrencyLostEvent event) {
        switch (event.getCurrency()) {
            case "Favor":
                Event playerFavorLostEvent = new SimplePlayerFavorLostEvent(event.getPlayerId(), event.getAmount(),
                        event.getNewCurrent());
                eventService.publish(playerFavorLostEvent);
                break;
            case "Gold":
                Event playerGoldLostEvent = new SimplePlayerGoldLostEvent(event.getPlayerId(), event.getAmount(),
                        event.getNewCurrent());
                eventService.publish(playerGoldLostEvent);
                break;
        }
    }
}
