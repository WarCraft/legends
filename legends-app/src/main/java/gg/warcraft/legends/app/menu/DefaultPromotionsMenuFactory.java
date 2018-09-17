package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.CollectablesMenuFactory;
import gg.warcraft.legends.api.menu.PromotionsMenuFactory;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.PromotionId;
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

public class DefaultPromotionsMenuFactory extends AbstractMenuFactory implements PromotionsMenuFactory {
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String ORANGE = ColorCode.GOLD.toString();

    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final CollectablesMenuFactory collectablesMenuFactory;

    @Inject
    public DefaultPromotionsMenuFactory(ShopQueryService shopQueryService,
                                        ShopProfileQueryService shopProfileQueryService,
                                        MenuCommandService menuCommandService,
                                        MenuBuilderFactory menuBuilderFactory,
                                        ButtonBuilderFactory buttonBuilderFactory,
                                        CollectablesMenuFactory collectablesMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.collectablesMenuFactory = collectablesMenuFactory;
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
        boolean hasUnlocked = shopProfile.hasPromotion(promotionId);
        Promotion promotion = shopQueryService.getPromotion(promotionId.toString());
        ButtonBuilder builder = createSimpleButtonBuilder(promotion.getIcon(), promotion.getName());
        promotion.getLore().forEach(builder::addTooltip);
        builder.addTooltip("");
        if (hasUnlocked) {
            builder.addTooltip(GREEN + "[CLICK] To take out a copy");
        } else {
            builder.addTooltip(ORANGE + "You haven't unlocked this promotion");
        }

        builder.withAction(action -> {
            if (hasUnlocked) {
                System.out.println("DEBUG tried to take out a copy of " + promotionId);
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
        builder.addButton(2, createPromotionButton(PromotionId.FOUR_YEAR_OLD_CAKE, shopProfile));
        builder.addButton(8, createBackButton(playerId, collectablesMenuFactory::createCollectablesMenu));
        return builder.build();
    }
}
