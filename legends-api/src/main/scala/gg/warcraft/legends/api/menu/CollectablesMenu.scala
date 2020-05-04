package gg.warcraft.legends.api.menu

object CollectablesMenu {

}

/*
public class DefaultCollectablesMenuFactory extends AbstractMenuFactory implements CollectablesMenuFactory {
    private final DecorationsMenuFactory decorationsMenuFactory;
    private final MiniPetsMenuFactory miniPetsMenuFactory;
    private final PromotionsMenuFactory promotionsMenuFactory;
    private final MainMenuFactory mainMenuFactory;

    @Inject
    public DefaultCollectablesMenuFactory(MenuCommandService menuCommandService, MenuBuilderFactory menuBuilderFactory,
                                          ButtonBuilderFactory buttonBuilderFactory,
                                          DecorationsMenuFactory decorationsMenuFactory,
                                          MiniPetsMenuFactory miniPetsMenuFactory,
                                          PromotionsMenuFactory promotionsMenuFactory,
                                          MainMenuFactory mainMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.decorationsMenuFactory = decorationsMenuFactory;
        this.miniPetsMenuFactory = miniPetsMenuFactory;
        this.promotionsMenuFactory = promotionsMenuFactory;
        this.mainMenuFactory = mainMenuFactory;
    }

    Button createInfoButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Your collectables");
        builder.addTooltip("From housing decorations to mini pets");
        builder.addTooltip("and more. There's a lot to collect on");
        builder.addTooltip("the server. Gotta collect 'em all!");
        return builder.build();
    }

    Button createDecorationsButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.PLANKS, "Decorations");
        builder.addTooltip("Did you know you can rent a house?");
        builder.addTooltip("Did you know you can completely");
        builder.addTooltip("decorate it however you want!?");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your decorations");

        builder.withAction(action -> {
            Menu menu = decorationsMenuFactory.createDecorationsMenu(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createMiniPetsButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.SPAWN_EGG, "Mini Pets"); // TODO was PIG variant
        builder.addTooltip("Mini pets are cute little creatures");
        builder.addTooltip("that will follow and cheer you on");
        builder.addTooltip("wherever you go.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your mini pets");

        builder.withAction(action -> {
            Menu menu = miniPetsMenuFactory.createMiniPetsMenu(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createPromotionsButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.CAKE, "Promotions");
        builder.addTooltip("Unique items that commemorate a");
        builder.addTooltip("special occasion. Promotions can");
        builder.addTooltip("generally only be obtained for a");
        builder.addTooltip("limited amount of time.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your promotions");

        builder.withAction(action -> {
            Menu menu = promotionsMenuFactory.createPromotionsMenu(playerId);
            showMenu(menu, playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createCollectablesMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("Collectables", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton(playerId));
        builder.addButton(2, createDecorationsButton(playerId));
        builder.addButton(3, createMiniPetsButton(playerId));
        builder.addButton(4, createPromotionsButton(playerId));
        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));
        return builder.build();
    }
}

 */
