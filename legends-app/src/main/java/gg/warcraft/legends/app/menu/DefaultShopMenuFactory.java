package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.ShopMenuFactory;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.SpecializationId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.service.ShopRepository;
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

public class DefaultShopMenuFactory extends AbstractMenuFactory implements ShopMenuFactory {
    private final LegendsPlayerQueryService playerQueryService;
    private final ShopProfileCommandService shopProfileCommandService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final ShopRepository shopRepository;
    private final MainMenuFactory mainMenuFactory;

    @Inject
    public DefaultShopMenuFactory(MenuCommandService menuCommandService,
                                  LegendsPlayerQueryService playerQueryService,
                                  ShopProfileCommandService shopProfileCommandService,
                                  ShopProfileQueryService shopProfileQueryService,
                                  ShopRepository shopRepository,
                                  MenuBuilderFactory menuBuilderFactory,
                                  ButtonBuilderFactory buttonBuilderFactory,
                                  MainMenuFactory mainMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.playerQueryService = playerQueryService;
        this.shopProfileCommandService = shopProfileCommandService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.shopRepository = shopRepository;
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
        builder.addTooltip("server the WarCraft: Shop has ten different");
        builder.addTooltip("items for sale every day. These items can");
        builder.addTooltip("be bought with Fragments which are available");
        builder.addTooltip("from the web shop at " + ColorCode.YELLOW + "shop.warcraft.gg");
        return builder.build();
    }

    Button createItemButton(ShopItem item, LegendsPlayer player, ShopProfile profile) {
        ButtonBuilder builder = createSimpleButtonBuilder(item.getIcon(), item.getName());
        builder.addTooltip(item.getType().toString());

        boolean hasUnlocked;
        switch (item.getType()) {
            case DECORATION:
                DecorationId decorationId = DecorationId.valueOf(item.getId());
                hasUnlocked = profile.hasDecoration(decorationId);
                break;
            case DECORATION_PACK:
                // TODO
                hasUnlocked = false;
                break;
            case MINI_PET:
                MiniPetId miniPetId = MiniPetId.valueOf(item.getId());
                hasUnlocked = profile.hasMiniPet(miniPetId);
                break;
            case SPECIALIZATION:
                SpecializationId specializationId = SpecializationId.valueOf(item.getId());
                hasUnlocked = profile.hasSpecialization(specializationId);
                break;
            case PROMOTION:
                PromotionId promotionId = PromotionId.valueOf(item.getId());
                hasUnlocked = profile.hasPromotion(promotionId);
                break;
            default:
                hasUnlocked = false;
                break;
        }

        boolean hasFavor = item.getPrice() <= player.getFragments();

        builder.addTooltip("");
        if (hasUnlocked) {
            builder.addTooltip("You already have this item unlocked.");
        } else if (hasFavor) {
            builder.addTooltip(ColorCode.GREEN + "[SHIFT RIGHT CLICK] To unlock for " + item.getPrice() + " Fragments");
        } else {
            builder.addTooltip(ColorCode.RED + "Costs " + item.getPrice() + " Fragments to unlock");
        }

        builder.withAction(click -> {
            if (hasUnlocked) {
                return;
            }

            if (click.isShiftRightClick()) {
                if (hasFavor) {
                    switch (item.getType()) {
                        case DECORATION:
                            DecorationId decorationId = DecorationId.valueOf(item.getId());
                            shopProfileCommandService.unlockDecoration(decorationId, player.getId(), true);
                            break;
                        case DECORATION_PACK:
                            DecorationPackId decorationPackId = DecorationPackId.valueOf(item.getId());
                            shopProfileCommandService.unlockDecorationPack(decorationPackId, player.getId(), true);
                            break;
                        case MINI_PET:
                            MiniPetId miniPetId = MiniPetId.valueOf(item.getId());
                            shopProfileCommandService.unlockMiniPet(miniPetId, player.getId(), true);
                            break;
                        case SPECIALIZATION:
                            SpecializationId specializationId = SpecializationId.valueOf(item.getId());
                            shopProfileCommandService.unlockSpecialization(specializationId, player.getId(), true);
                            break;
                        case PROMOTION:
                            PromotionId promotionId = PromotionId.valueOf(item.getId());
                            shopProfileCommandService.unlockPromotion(promotionId, player.getId(), true);
                            break;
                    }
                }
            }
        });

        return builder.build();
    }

    @Override
    public Menu createShopMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        ShopProfile profile = shopProfileQueryService.getProfile(playerId);

        MenuBuilder builder = createMenuBuilder("WarCraft: Shop", MenuSize.TWO_ROWS);
        builder.addButton(0, createInfoButton());

        int slot = 2;
        for (ShopItem item : shopRepository.getAvailableItems()) {
            builder.addButton(slot, createItemButton(item, player, profile));
            slot += 1;
            if (slot == 7) {
                slot = 11;
            }
        }

        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));
        return builder.build();
    }
}
