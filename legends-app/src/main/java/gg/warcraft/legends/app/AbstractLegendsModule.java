package gg.warcraft.legends.app;

import com.google.inject.AbstractModule;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerProfileRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerCommandService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerProfileRepository;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerQueryService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerProfileRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;

public abstract class AbstractLegendsModule extends AbstractModule {

    @Override
    protected void configure() {
        configureEntity();
    }

    private void configureEntity() {
        bind(PlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        bind(LegendsPlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        bind(PlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
        bind(LegendsPlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
        bind(PlayerProfileRepository.class).to(DefaultLegendsPlayerProfileRepository.class);
        bind(LegendsPlayerProfileRepository.class).to(DefaultLegendsPlayerProfileRepository.class);
    }
}
