/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

object DecorationPackMenu {

}


/*
public class DefaultDecorationPackMenuFactory extends AbstractMenuFactory implements DecorationPackMenuFactory {
    private final ShopProfileQueryService shopProfileQueryService;
    private final ShopQueryService shopQueryService;
    private final PlayerCommandService playerCommandService;
    private final DecorationBuilderFactory decorationBuilderFactory;
    private final LegendsMenuFactory legendsMenuFactory;

    @Inject
    public DefaultDecorationPackMenuFactory(ShopProfileQueryService shopProfileQueryService,
                                            ShopQueryService shopQueryService,
                                            PlayerCommandService playerCommandService,
                                            LegendsMenuFactory legendsMenuFactory,
                                            MenuCommandService menuCommandService,
                                            MenuBuilderFactory menuBuilderFactory,
                                            DecorationBuilderFactory decorationBuilderFactory,
                                            ButtonBuilderFactory buttonBuilderFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.shopProfileQueryService = shopProfileQueryService;
        this.shopQueryService = shopQueryService;
        this.playerCommandService = playerCommandService;
        this.decorationBuilderFactory = decorationBuilderFactory;
        this.legendsMenuFactory = legendsMenuFactory;
    }

    Button createInfoButton(DecorationPack pack) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, pack.getName());
        builder.withTooltip(pack.getLore());
        return builder.build();
    }

    Button createDecorationButton(Decoration decoration, ShopProfile profile) {
        DecorationId decorationId = DecorationId.valueOf(decoration.getId());
        boolean hasDecoration = profile.isGrandfathered() || profile.hasDecoration(decorationId);

        ButtonBuilder builder = createSimpleButtonBuilder(decoration.getIcon(), decoration.getName());
        builder.withTooltip(decoration.getLore());
        builder.addTooltip("");
        if (hasDecoration) {
            builder.addTooltip(ColorCode.GREEN + "[CLICK] To take out a stack of " + decoration.getName());
        } else {
            builder.addTooltip(ColorCode.RED + "Locked");
        }

        builder.withAction(click -> {
            if (hasDecoration) {
                Item itemStack = ((StackableItem) decorationBuilderFactory.createDecorationBuilder(decoration).build()).withCount(64);
                playerCommandService.giveItem(profile.getPlayerId(), itemStack, false);
            }
        });
        return builder.build();
    }

    @Override
    public Menu createDecorationItemsMenu(UUID playerId, DecorationPackId decorationPackId) {
        DecorationPack pack = shopQueryService.getDecorationPack(decorationPackId.toString());
        List<Decoration> decorations = pack.getDecorations();
        int rowCount = 2;
        if (decorations.size() > 14) {
            rowCount = (int) Math.ceil(decorations.size() / 7f);
        }
        MenuSize menuSize = MenuSize.fromRows(rowCount);

        ShopProfile profile = shopProfileQueryService.getProfile(playerId);
        MenuBuilder builder = createMenuBuilder(pack.getName(), menuSize);
        builder.addButton(0, createInfoButton(pack));
        builder.addButton(9, createBackButton(playerId, legendsMenuFactory::createDecorationsMenu));

        int slot = 2;
        for (Decoration decoration : decorations) {
            if (slot % 9 == 0) {
                slot += 2;
            }
            builder.addButton(slot, createDecorationButton(decoration, profile));
            slot += 1;
        }
        return builder.build();
    }
}

 */