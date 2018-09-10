package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.DecorationsMenuFactory;
import gg.warcraft.legends.api.menu.LegendsMenuFactory;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
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

public class DefaultDecorationsMenuFactory implements DecorationsMenuFactory {
    private static final String GREEN = ColorCode.GREEN.toString();

    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final MenuCommandService menuCommandService;
    private final LegendsMenuFactory legendsMenuFactory;
    private final MenuBuilderFactory menuBuilderFactory;
    private final ButtonBuilderFactory buttonBuilderFactory;

    @Inject
    public DefaultDecorationsMenuFactory(ShopQueryService shopQueryService,
                                         ShopProfileQueryService shopProfileQueryService,
                                         MenuCommandService menuCommandService, LegendsMenuFactory legendsMenuFactory,
                                         MenuBuilderFactory menuBuilderFactory,
                                         ButtonBuilderFactory buttonBuilderFactory) {
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.menuCommandService = menuCommandService;
        this.legendsMenuFactory = legendsMenuFactory;
        this.menuBuilderFactory = menuBuilderFactory;
        this.buttonBuilderFactory = buttonBuilderFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BOOK, "Decorations");
        builder.addTooltip("Did you know you can rent a house?");
        builder.addTooltip("Did you know you can completely");
        builder.addTooltip("decorate it however you want!?");
        builder.addTooltip("");
        builder.addTooltip("You can unlock decorations via the");
        builder.addTooltip("shop or a loot crate.");
        return builder.build();
    }

    Button createDecorationButton(DecorationPackId packId, ShopProfile shopProfile) {
        DecorationPack pack = shopQueryService.getDecorationPack(packId.toString());
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(pack.getIcon(), pack.getName());
        pack.getLore().forEach(builder::addTooltip);
        builder.addTooltip("");
        builder.addTooltip(GREEN + "[CLICK] To browse " + pack.getName() + " decorations");

        builder.withAction(action -> {
            UUID playerId = shopProfile.getPlayerId();
            Menu packMenu = legendsMenuFactory.createDecorationPackMenu(playerId);
            menuCommandService.showMenu(packMenu, playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createDecorationsMenu(UUID playerId) {
        ShopProfile shopProfile = shopProfileQueryService.getProfile(playerId);
        MenuBuilder builder = menuBuilderFactory.createMenuBuilder("Decorations", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());
        builder.addButton(2, createDecorationButton(DecorationPackId.BASIC, shopProfile));
        return builder.build();
    }
}
