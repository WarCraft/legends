package gg.warcraft.legends.api.menu

object MiniPetsMenu {

}

/*
public class DefaultMiniPetsMenuFactory extends AbstractMenuFactory implements MiniPetsMenuFactory {
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String ORANGE = ColorCode.GOLD.toString();

    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final MiniPetFactory miniPetFactory;
    private final CollectablesMenuFactory collectablesMenuFactory;

    @Inject
    public DefaultMiniPetsMenuFactory(ShopQueryService shopQueryService,
                                      ShopProfileQueryService shopProfileQueryService,
                                      MenuCommandService menuCommandService,
                                      MenuBuilderFactory menuBuilderFactory,
                                      ButtonBuilderFactory buttonBuilderFactory,
                                      MiniPetFactory miniPetFactory,
                                      CollectablesMenuFactory collectablesMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.miniPetFactory = miniPetFactory;
        this.collectablesMenuFactory = collectablesMenuFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Mini Pets");
        builder.addTooltip("Mini pets are cute little creatures");
        builder.addTooltip("that will follow and cheer you on");
        builder.addTooltip("wherever you go.");
        builder.addTooltip("");
        builder.addTooltip("You can unlock mini pets via the shop");
        builder.addTooltip("or a loot crate.");
        return builder.build();
    }

    Button createPetButton(MiniPetId miniPetId, ShopProfile profile) {
        UUID playerId = profile.getPlayerId();
        boolean hasUnlocked = profile.isGrandfathered() || profile.hasMiniPet(miniPetId);
        MiniPet miniPet = shopQueryService.getMiniPet(miniPetId.toString());
        ButtonBuilder builder = createSkullButtonBuilder(miniPet.getSkull(), miniPet.getName());
        miniPet.getLore().forEach(builder::addTooltip);
        builder.addTooltip("");
        if (hasUnlocked) {
            builder.addTooltip(GREEN + "[CLICK] To summon");
        } else {
            builder.addTooltip(ORANGE + "You haven't unlocked this mini yet");
        }

        builder.withAction(action -> {
            if (hasUnlocked) {
                miniPetFactory.spawnPet(miniPet.getEntityType(), playerId);
                closeMenu(playerId);
            }
        });
        return builder.build();
    }

    Button createDismissButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BARRIER, "Dismiss");
        builder.addTooltip("[CLICK] To dismiss");
        builder.addTooltip("your current pet");
        builder.withAction(action -> {
            miniPetFactory.removePet(playerId);
            closeMenu(playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createMiniPetsMenu(UUID playerId) {
        ShopProfile shopProfile = shopProfileQueryService.getProfile(playerId);
        MenuBuilder builder = createMenuBuilder("Mini Pets", MenuSize.TWO_ROWS);
        builder.addButton(0, createInfoButton());
        builder.addButton(2, createPetButton(MiniPetId.BAT, shopProfile));
        builder.addButton(3, createPetButton(MiniPetId.CHICKEN, shopProfile));
        builder.addButton(4, createPetButton(MiniPetId.COW, shopProfile));
        builder.addButton(5, createPetButton(MiniPetId.LLAMA, shopProfile));
        builder.addButton(6, createPetButton(MiniPetId.OCELOT, shopProfile));
        builder.addButton(7, createPetButton(MiniPetId.PIG, shopProfile));
        builder.addButton(8, createPetButton(MiniPetId.RABBIT, shopProfile));
        builder.addButton(11, createPetButton(MiniPetId.SHEEP, shopProfile));
        builder.addButton(12, createPetButton(MiniPetId.SILVERFISH, shopProfile));
        builder.addButton(13, createPetButton(MiniPetId.WOLF, shopProfile));
        builder.addButton(14, createDismissButton(playerId));
        builder.addButton(9, createBackButton(playerId, collectablesMenuFactory::createCollectablesMenu));
        return builder.build();
    }
}

 */