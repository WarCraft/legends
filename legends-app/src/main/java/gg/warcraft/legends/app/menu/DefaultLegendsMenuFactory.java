package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.legends.api.menu.DecorationPackMenuFactory;
import gg.warcraft.legends.api.menu.DecorationsMenuFactory;
import gg.warcraft.legends.api.menu.LegendsMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.MiniPetsMenuFactory;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
import gg.warcraft.legends.api.menu.TeamMenuFactory;
import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public class DefaultLegendsMenuFactory implements LegendsMenuFactory {
    private final MainMenuFactory mainMenuFactory;
    private final SpecializationMenuFactory specializationMenuFactory;
    private final TeamMenuFactory teamMenuFactory;
    private final DailyLoginMenuFactory dailyLoginMenuFactory;
    private final DecorationsMenuFactory decorationsMenuFactory;
    private final DecorationPackMenuFactory decorationPackMenuFactory;
    private final MiniPetsMenuFactory miniPetsMenuFactory;

    @Inject
    public DefaultLegendsMenuFactory(MainMenuFactory mainMenuFactory,
                                     SpecializationMenuFactory specializationMenuFactory,
                                     TeamMenuFactory teamMenuFactory, DailyLoginMenuFactory dailyLoginMenuFactory,
                                     DecorationsMenuFactory decorationsMenuFactory,
                                     DecorationPackMenuFactory decorationPackMenuFactory,
                                     MiniPetsMenuFactory miniPetsMenuFactory) {
        this.mainMenuFactory = mainMenuFactory;
        this.specializationMenuFactory = specializationMenuFactory;
        this.teamMenuFactory = teamMenuFactory;
        this.dailyLoginMenuFactory = dailyLoginMenuFactory;
        this.decorationsMenuFactory = decorationsMenuFactory;
        this.decorationPackMenuFactory = decorationPackMenuFactory;
        this.miniPetsMenuFactory = miniPetsMenuFactory;
    }

    @Override
    public Menu createMainMenu(UUID playerId) {
        return mainMenuFactory.createMainMenu(playerId);
    }

    @Override
    public Menu createSpecializationMenu(UUID playerId) {
        return specializationMenuFactory.createSpecializationMenu(playerId);
    }

    @Override
    public Menu createTeamMenu(UUID playerId) {
        return teamMenuFactory.createTeamMenu(playerId);
    }

    @Override
    public Menu createDailyLoginMenu(UUID playerId) {
        return dailyLoginMenuFactory.createDailyLoginMenu(playerId);
    }

    @Override
    public Menu createDecorationsMenu(UUID playerId) {
        return decorationsMenuFactory.createDecorationsMenu(playerId);
    }

    @Override
    public Menu createDecorationPackMenu(UUID playerId) {
        return decorationPackMenuFactory.createDecorationItemsMenu(playerId);
    }

    @Override
    public Menu createMiniPetsMenu(UUID playerId) {
        return miniPetsMenuFactory.createMiniPetsMenu(playerId);
    }
}
