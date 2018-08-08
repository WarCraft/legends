package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.entity.player.service.PlayerProfileRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerServerAdapter;
import gg.warcraft.monolith.app.entity.player.service.DefaultPlayerCommandService;

import java.util.UUID;

public class DefaultLegendsPlayerCommandService extends DefaultPlayerCommandService implements LegendsPlayerCommandService {

    @Inject
    public DefaultLegendsPlayerCommandService(PlayerProfileRepository playerProfileRepository,
                                              PlayerServerAdapter playerServerAdapter, EventService eventService) {
        super(playerProfileRepository, playerServerAdapter, eventService);
    }

    @Override
    public void addGold(UUID playerId, int amount) {
        super.addCurrency(playerId, LegendsCurrency.GOLD, amount);
    }

    @Override
    public void removeGold(UUID playerId, int amount) {
        super.removeCurrency(playerId, LegendsCurrency.GOLD, amount);
    }

    @Override
    public void addFavor(UUID playerId, int amount) {
        super.addCurrency(playerId, LegendsCurrency.FAVOR, amount);

    }

    @Override
    public void removeFavor(UUID playerId, int amount) {
        super.removeCurrency(playerId, LegendsCurrency.FAVOR, amount);

    }
}
