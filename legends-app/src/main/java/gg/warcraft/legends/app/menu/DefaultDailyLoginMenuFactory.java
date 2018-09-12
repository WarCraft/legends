package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuSize;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;

import java.util.UUID;

public class DefaultDailyLoginMenuFactory extends AbstractMenuFactory implements DailyLoginMenuFactory {
    private final MainMenuFactory mainMenuFactory;

    @Inject
    public DefaultDailyLoginMenuFactory(MenuCommandService menuCommandService,
                                        MenuBuilderFactory menuBuilderFactory,
                                        ButtonBuilderFactory buttonBuilderFactory,
                                        MainMenuFactory mainMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.mainMenuFactory = mainMenuFactory;
    }

    @Override
    public Menu createDailyLoginMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("Daily Login", MenuSize.FOUR_ROWS);
        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));
        return builder.build();
    }
}
