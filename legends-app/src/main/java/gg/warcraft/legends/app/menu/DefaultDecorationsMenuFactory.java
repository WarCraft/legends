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
import gg.warcraft.monolith.api.menu.AbstractMenuFactory;
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

public class DefaultDecorationsMenuFactory extends AbstractMenuFactory implements DecorationsMenuFactory {
    private static final String GREEN = ColorCode.GREEN.toString();

    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final LegendsMenuFactory legendsMenuFactory;

    @Inject
    public DefaultDecorationsMenuFactory(ShopQueryService shopQueryService,
                                         ShopProfileQueryService shopProfileQueryService,
                                         MenuCommandService menuCommandService, LegendsMenuFactory legendsMenuFactory,
                                         MenuBuilderFactory menuBuilderFactory,
                                         ButtonBuilderFactory buttonBuilderFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.legendsMenuFactory = legendsMenuFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Decorations");
        builder.addTooltip("Did you know you can rent a house?");
        builder.addTooltip("Did you know you can completely");
        builder.addTooltip("decorate it however you want!?");
        builder.addTooltip("");
        builder.addTooltip("You can unlock decorations via the");
        builder.addTooltip("shop and opening loot crates.");
        return builder.build();
    }

    Button createDecorationButton(DecorationPackId packId, ShopProfile shopProfile) {
        DecorationPack pack = shopQueryService.getDecorationPack(packId.toString());
        ButtonBuilder builder = createSimpleButtonBuilder(pack.getIcon(), pack.getName());
        pack.getLore().forEach(builder::addTooltip);
        builder.addTooltip("");
        builder.addTooltip(GREEN + "[CLICK] To browse your " + pack.getName() + " decorations");

        builder.withAction(action -> {
            UUID playerId = shopProfile.getPlayerId();
            Menu packMenu = legendsMenuFactory.createDecorationPackMenu(playerId);
            showMenu(packMenu, playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createDecorationsMenu(UUID playerId) {
        ShopProfile shopProfile = shopProfileQueryService.getProfile(playerId);
        MenuBuilder builder = createMenuBuilder("Decorations", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());
        builder.addButton(9, createBackButton(playerId, legendsMenuFactory::createCollectablesMenuFactory));

        builder.addButton(2, createDecorationButton(DecorationPackId.BASIC, shopProfile));
        builder.addButton(3, createDecorationButton(DecorationPackId.FABRICS, shopProfile));
        builder.addButton(4, createDecorationButton(DecorationPackId.GLASS, shopProfile));
        builder.addButton(5, createDecorationButton(DecorationPackId.FLOWERS, shopProfile));
        builder.addButton(6, createDecorationButton(DecorationPackId.GARDENING, shopProfile));
        builder.addButton(7, createDecorationButton(DecorationPackId.STONE, shopProfile));
        builder.addButton(8, createDecorationButton(DecorationPackId.DESERT, shopProfile));

        builder.addButton(11, createDecorationButton(DecorationPackId.WOOD, shopProfile));
        builder.addButton(12, createDecorationButton(DecorationPackId.NATURE, shopProfile));
        builder.addButton(13, createDecorationButton(DecorationPackId.ORES, shopProfile));
        builder.addButton(14, createDecorationButton(DecorationPackId.GEMSTONES, shopProfile));
        builder.addButton(15, createDecorationButton(DecorationPackId.CLAY, shopProfile));
        builder.addButton(16, createDecorationButton(DecorationPackId.ART, shopProfile));
        builder.addButton(17, createDecorationButton(DecorationPackId.MUSIC, shopProfile));

        // builder.addButton(20, createDecorationButton(DecorationPackId.?, shopProfile));
        builder.addButton(21, createDecorationButton(DecorationPackId.CONCRETE, shopProfile));
        builder.addButton(22, createDecorationButton(DecorationPackId.TERRACOTTA, shopProfile));
        builder.addButton(23, createDecorationButton(DecorationPackId.BLURPLE, shopProfile));
        builder.addButton(24, createDecorationButton(DecorationPackId.ICE_AND_FIRE, shopProfile));
        builder.addButton(25, createDecorationButton(DecorationPackId.NETHER, shopProfile));
        builder.addButton(26, createDecorationButton(DecorationPackId.MOB_HEADS, shopProfile));
        return builder.build();
    }
}
