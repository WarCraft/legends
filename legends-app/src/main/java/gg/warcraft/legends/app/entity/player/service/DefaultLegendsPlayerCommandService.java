package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandServiceDecorator;

import java.util.UUID;

public class DefaultLegendsPlayerCommandService implements LegendsPlayerCommandService, PlayerCommandServiceDecorator {
    private final PlayerCommandService playerCommandService;

    @Inject
    public DefaultLegendsPlayerCommandService(PlayerCommandService playerCommandService) {
        this.playerCommandService = playerCommandService;
    }

    @Override
    public PlayerCommandService getPlayerCommandService() {
        return playerCommandService;
    }

    @Override
    public void addGold(UUID playerId, int amount) {
        addCurrency(playerId, LegendsCurrency.GOLD, amount);
    }

    @Override
    public void removeGold(UUID playerId, int amount) {
        removeCurrency(playerId, LegendsCurrency.GOLD, amount);
    }

    @Override
    public void addFavor(UUID playerId, int amount) {
        addCurrency(playerId, LegendsCurrency.FAVOR, amount);

    }

    @Override
    public void removeFavor(UUID playerId, int amount) {
        removeCurrency(playerId, LegendsCurrency.FAVOR, amount);
    }
}
