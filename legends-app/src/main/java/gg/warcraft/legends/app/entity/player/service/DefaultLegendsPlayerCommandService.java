package gg.warcraft.legends.app.entity.player.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerProfileRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerServerAdapter;
import gg.warcraft.monolith.app.entity.player.service.DefaultPlayerCommandService;

public class DefaultLegendsPlayerCommandService extends DefaultPlayerCommandService implements LegendsPlayerCommandService {

    @Inject
    public DefaultLegendsPlayerCommandService(PlayerProfileRepository playerProfileRepository,
                                              PlayerServerAdapter playerServerAdapter) {
        super(playerProfileRepository, playerServerAdapter);
    }
}
