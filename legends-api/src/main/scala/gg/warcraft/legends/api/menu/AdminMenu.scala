package gg.warcraft.legends.api.menu

import gg.warcraft.monolith.api.menu.Menu

object AdminMenu {
  def apply(): Menu = {
    
  }
}


/*
public class DefaultAdminMenuFactory extends AbstractMenuFactory implements AdminMenuFactory {
    private final PlayerCommandService playerCommandService;
    private final AuthorizationService authorizationService;
    private final LootCrateRepository lootCrateRepository;
    private final LootCrateBuilderFactory lootCrateBuilderFactory;

    @Inject
    public DefaultAdminMenuFactory(PlayerCommandService playerCommandService,
                                   AuthorizationService authorizationService,
                                   LootCrateRepository lootCrateRepository,
                                   LootCrateBuilderFactory lootCrateBuilderFactory,
                                   MenuCommandService menuCommandService,
                                   MenuBuilderFactory menuBuilderFactory,
                                   ButtonBuilderFactory buttonBuilderFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.playerCommandService = playerCommandService;
        this.authorizationService = authorizationService;
        this.lootCrateRepository = lootCrateRepository;
        this.lootCrateBuilderFactory = lootCrateBuilderFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Admin Menu");
        builder.addTooltip("A convenience menu for");
        builder.addTooltip("members of staff.");
        return builder.build();
    }

    Button createLootCrateButton(UUID playerId, LootCrateType crateType) {
        LootCrate crate = lootCrateRepository.get(crateType);
        ButtonBuilder buttonBuilder = createSimpleButtonBuilder(crate.getItemType(), crate.getName());
        buttonBuilder.addTooltip("");
        buttonBuilder.addTooltip("[CLICK] To take out a " + crate.getName());
        buttonBuilder.addTooltip("[SHIFT CLICK] To take out a stack of " + crate.getName());

        buttonBuilder.withAction(action -> {
            // TODO    if (authorizationService.isAdmin(playerId)) {
            LootCrateBuilder crateBuilder = lootCrateBuilderFactory.createLootCrateBuilder(crateType);
            Item crateItem = ((StackableItem) crateBuilder.build()).withCount(action.isShiftClick() ? 64 : 1);
            playerCommandService.giveItem(playerId, crateItem, false);
            // TODO    }
        });
        return buttonBuilder.build();
    }

    @Override
    public Menu createFishMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("Admin Menu", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());
        // TODO    if (!authorizationService.isAdmin(playerId)) {
        // TODO            return builder.build();
        // TODO   }

        builder.addButton(2, createLootCrateButton(playerId, LootCrateType.WAR_CRATE));
        builder.addButton(3, createLootCrateButton(playerId, LootCrateType.LEGEND_CRATE));
        builder.addButton(4, createLootCrateButton(playerId, LootCrateType.TITAN_CRATE));
        builder.addButton(5, createLootCrateButton(playerId, LootCrateType.VOTE_CRATE));
        return builder.build();
    }
}
 */