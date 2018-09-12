package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.ShopMenuFactory;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.ButtonBuilder;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuSize;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.UUID;

public class DefaultShopMenuFactory extends AbstractMenuFactory implements ShopMenuFactory {
    private final MainMenuFactory mainMenuFactory;

    @Inject
    public DefaultShopMenuFactory(MenuCommandService menuCommandService,
                                  MenuBuilderFactory menuBuilderFactory,
                                  ButtonBuilderFactory buttonBuilderFactory,
                                  MainMenuFactory mainMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.mainMenuFactory = mainMenuFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "WarCraft: Shop");
        builder.addTooltip("Running a server is a lot of fun and super");
        builder.addTooltip("rewarding, but it also requires large amounts");
        builder.addTooltip("of time and funds. Purchasing items in the");
        builder.addTooltip("shop is entirely optional as everything can");
        builder.addTooltip("also be obtained via loot crates that you get");
        builder.addTooltip("for simply playing on the server.");
        builder.addTooltip("");
        builder.addTooltip("To go even further beyond in supporting the");
        builder.addTooltip("server the WarCraft: Shop has six different");
        builder.addTooltip("items for sale every day. These items can");
        builder.addTooltip("be bought with Fragments which are available");
        builder.addTooltip("from the web shop at " + ColorCode.YELLOW + "shop.warcraft.gg");
        return builder.build();
    }

    @Override
    public Menu createShopMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("WarCraft: Shop", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());
        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));
        return builder.build();
    }
}
