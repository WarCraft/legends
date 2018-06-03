package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.app.entity.player.event.SimplePlayerFavorEarnedEvent;
import gg.warcraft.monolith.api.event.EventService;

import java.util.UUID;

public class DefaultLegendsPlayerCommandService implements LegendsPlayerCommandService {
    private final EventService eventService;

    @Inject
    public DefaultLegendsPlayerCommandService(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void addVote(UUID playerId) {
        // TODO can these 2 be refactored back into Monolith? AbstractPlayerCommandService?
        System.out.println("Attempted to add vote for payer with id " + playerId);
    }

    @Override
    public void updateTimePlayed(UUID playerId) {
        // TODO
        System.out.println("Attempted to update play time for payer with id " + playerId);
    }

    @Override
    public void addGoldCoins(UUID playerId, int amount) {
        // add to coins and total coins
    }

    @Override
    public void subtractGoldCoins(UUID playerId, int amount) {
        // subtract from coins but not total coins
    }

    @Override
    public void addFavor(UUID playerId, int amount) {
        // add to favor and totalfavor
        eventService.publish(new SimplePlayerFavorEarnedEvent(playerId));
    }

    @Override
    public void subtractFavor(UUID playerId, int amount) {
        // only remove from favor not total favor
    }
}
