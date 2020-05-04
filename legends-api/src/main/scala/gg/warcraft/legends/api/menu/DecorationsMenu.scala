package gg.warcraft.legends.api.menu

object DecorationsMenu {

}

/*
public class DefaultDecorationsMenuFactory extends AbstractMenuFactory implements DecorationsMenuFactory {
    private static final String GREEN = ColorCode.GREEN.toString();

    private final ShopQueryService shopQueryService;
    private final LegendsMenuFactory legendsMenuFactory;

    @Inject
    public DefaultDecorationsMenuFactory(ShopQueryService shopQueryService,
                                         MenuCommandService menuCommandService,
                                         LegendsMenuFactory legendsMenuFactory,
                                         MenuBuilderFactory menuBuilderFactory,
                                         ButtonBuilderFactory buttonBuilderFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.shopQueryService = shopQueryService;
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

    Button createDecorationButton(UUID playerId, DecorationPackId decorationPackId) {
        DecorationPack pack = shopQueryService.getDecorationPack(decorationPackId.toString());
        ButtonBuilder builder = createSimpleButtonBuilder(pack.getIcon(), pack.getName());
        pack.getLore().forEach(builder::addTooltip);
        builder.addTooltip("");
        builder.addTooltip(GREEN + "[CLICK] To browse your " + pack.getName() + " decorations");

        builder.withAction(action -> {
            Menu packMenu = legendsMenuFactory.createDecorationPackMenu(playerId, decorationPackId);
            showMenu(packMenu, playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createDecorationsMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("Decorations", MenuSize.THREE_ROWS);
        builder.addButton(0, createInfoButton());
        builder.addButton(9, createBackButton(playerId, legendsMenuFactory::createCollectablesMenuFactory));

        builder.addButton(2, createDecorationButton(playerId, DecorationPackId.BASIC));
        builder.addButton(3, createDecorationButton(playerId, DecorationPackId.FABRICS));
        builder.addButton(4, createDecorationButton(playerId, DecorationPackId.GLASS));
        builder.addButton(5, createDecorationButton(playerId, DecorationPackId.STONE));
        builder.addButton(6, createDecorationButton(playerId, DecorationPackId.WOOD));
        builder.addButton(7, createDecorationButton(playerId, DecorationPackId.DESERT));
        builder.addButton(8, createDecorationButton(playerId, DecorationPackId.TERRACOTTA));

        builder.addButton(11, createDecorationButton(playerId, DecorationPackId.NATURE));
        builder.addButton(12, createDecorationButton(playerId, DecorationPackId.GARDENING));
        builder.addButton(13, createDecorationButton(playerId, DecorationPackId.FLOWERS));
        builder.addButton(14, createDecorationButton(playerId, DecorationPackId.ORES));
        builder.addButton(15, createDecorationButton(playerId, DecorationPackId.GEMSTONES));
        builder.addButton(16, createDecorationButton(playerId, DecorationPackId.CONCRETE));
        builder.addButton(17, createDecorationButton(playerId, DecorationPackId.GLAZED_TERRACOTTA));

        builder.addButton(20, createDecorationButton(playerId, DecorationPackId.BLURPLE));
        builder.addButton(21, createDecorationButton(playerId, DecorationPackId.NETHER));
        builder.addButton(22, createDecorationButton(playerId, DecorationPackId.ICE_AND_FIRE));
        builder.addButton(23, createDecorationButton(playerId, DecorationPackId.ART));
        builder.addButton(24, createDecorationButton(playerId, DecorationPackId.MOB_HEADS));
        builder.addButton(25, createDecorationButton(playerId, DecorationPackId.MUSIC));
        return builder.build();
    }
}

 */
