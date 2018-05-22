package gg.warcraft.legends.app.entity.player.service;

import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;

import java.util.UUID;

public class DefaultLegendsPlayerCommandService implements LegendsPlayerCommandService {

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

    }

    @Override
    public void subtractGoldCoins(UUID playerId, int amount) {

    }

    @Override
    public void addFavor(UUID playerId, int amount) {

    }

    @Override
    public void subtractFavor(UUID playerId, int amount) {

    }
}
