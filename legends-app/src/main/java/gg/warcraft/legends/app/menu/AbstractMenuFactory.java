package gg.warcraft.legends.app.menu;

import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.ButtonBuilder;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuSize;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;

import java.util.UUID;
import java.util.function.Function;

public class AbstractMenuFactory {
    private final MenuCommandService menuCommandService;
    private final MenuBuilderFactory menuBuilderFactory;
    private final ButtonBuilderFactory buttonBuilderFactory;

    public AbstractMenuFactory(MenuCommandService menuCommandService,
                               MenuBuilderFactory menuBuilderFactory,
                               ButtonBuilderFactory buttonBuilderFactory) {
        this.menuCommandService = menuCommandService;
        this.menuBuilderFactory = menuBuilderFactory;
        this.buttonBuilderFactory = buttonBuilderFactory;
    }

    protected void showMenu(Menu menu, UUID playerId) {
        menuCommandService.showMenu(menu, playerId);
    }

    protected void closeMenu(UUID playerId) {
        menuCommandService.closeMenu(playerId);
    }

    protected MenuBuilder createMenuBuilder(String title, MenuSize size) {
        return menuBuilderFactory.createMenuBuilder(title, size);
    }

    protected ButtonBuilder createSimpleButtonBuilder(ItemType icon, String title) {
        return buttonBuilderFactory.createSimpleButtonBuilder(icon, title);
    }

    protected ButtonBuilder createSkullButtonBuilder(String skullName, String title) {
        return buttonBuilderFactory.createSkullButtonBuilder(skullName, title);
    }

    Button createBackButton(UUID playerId, Function<UUID, Menu> menuFactory) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.OAK_DOOR, "Back");
        builder.addTooltip("[CLICK] To go back to");
        builder.addTooltip("the previous menu");

        builder.withAction(action -> {
            Menu menu = menuFactory.apply(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }
}
