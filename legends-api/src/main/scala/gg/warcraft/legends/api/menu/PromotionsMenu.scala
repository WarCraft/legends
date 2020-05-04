package gg.warcraft.legends.api.menu

object PromotionsMenu {

}

/*
public class DefaultPromotionsMenuFactory extends AbstractMenuFactory implements PromotionsMenuFactory {
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String ORANGE = ColorCode.GOLD.toString();

    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final LegendsPlayerCommandService legendsPlayerCommandService;
    private final CollectablesMenuFactory collectablesMenuFactory;
    private final EquipmentBuilderFactory equipmentBuilderFactory;

    @Inject
    public DefaultPromotionsMenuFactory(ShopQueryService shopQueryService,
                                        ShopProfileQueryService shopProfileQueryService,
                                        LegendsPlayerQueryService legendsPlayerQueryService,
                                        LegendsPlayerCommandService legendsPlayerCommandService,
                                        MenuCommandService menuCommandService,
                                        MenuBuilderFactory menuBuilderFactory,
                                        ButtonBuilderFactory buttonBuilderFactory,
                                        CollectablesMenuFactory collectablesMenuFactory,
                                        EquipmentBuilderFactory equipmentBuilderFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.legendsPlayerCommandService = legendsPlayerCommandService;
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.collectablesMenuFactory = collectablesMenuFactory;
        this.equipmentBuilderFactory = equipmentBuilderFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Promotions");
        builder.addTooltip("Unique items that commemorate a");
        builder.addTooltip("special occasion. Promotions can");
        builder.addTooltip("generally only be obtained for a");
        builder.addTooltip("limited amount of time.");
        return builder.build();
    }

    Button createPromotionButton(PromotionId promotionId, ShopProfile shopProfile) {
        UUID playerId = shopProfile.getPlayerId();
        boolean hasUnlocked = shopProfile.isGrandfathered() || shopProfile.hasPromotion(promotionId);
        Promotion promotion = shopQueryService.getPromotion(promotionId.toString());
        ButtonBuilder builder = createSimpleButtonBuilder(promotion.getIcon(), promotion.getName());
        promotion.getLore().forEach(builder::addTooltip);
        builder.addTooltip("");
        if (hasUnlocked) {
            builder.addTooltip(GREEN + "[CLICK] To equip");
        } else {
            builder.addTooltip(ORANGE + "You haven't unlocked this promotion");
        }

        builder.withAction(action -> {
            if (hasUnlocked) {
                LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
                Equipment equipment = player.getEquipment();
                if (equipment.getSlot(EquipmentSlot.HEAD) == null) {
                    EquipmentBuilder itemBuilder = equipmentBuilderFactory.createEquipmentBuilder(
                            promotion.getItemType(), promotion.getRarity(), promotion.getName());
                    itemBuilder.withAttributes(promotion.getAttributes());
                    itemBuilder.withFlavorText(promotion.getLore());
                    legendsPlayerCommandService.setEquipment(playerId, EquipmentSlot.HEAD, itemBuilder.build());
                } else {
                    legendsPlayerCommandService.sendNotification(playerId, "Please remove your current helmet first");
                }
                closeMenu(playerId);
            }
        });
        return builder.build();
    }

    @Override
    public Menu createPromotionsMenu(UUID playerId) {
        ShopProfile shopProfile = shopProfileQueryService.getProfile(playerId);
        MenuBuilder builder = createMenuBuilder("Promotions", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());
        builder.addButton(2, createPromotionButton(PromotionId.FIVE_YEAR_OLD_CAKE, shopProfile));
        builder.addButton(8, createBackButton(playerId, collectablesMenuFactory::createCollectablesMenu));
        return builder.build();
    }
}
 */
