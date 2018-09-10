package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.MiniPetsMenuFactory;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.MiniPetId;
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
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.UUID;

public class DefaultMiniPetsMenuFactory implements MiniPetsMenuFactory {
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String ORANGE = ColorCode.GOLD.toString();

    private final ShopQueryService shopQueryService;
    private final ShopProfileQueryService shopProfileQueryService;
    private final MenuBuilderFactory menuBuilderFactory;
    private final ButtonBuilderFactory buttonBuilderFactory;

    @Inject
    public DefaultMiniPetsMenuFactory(ShopQueryService shopQueryService,
                                      ShopProfileQueryService shopProfileQueryService,
                                      MenuBuilderFactory menuBuilderFactory,
                                      ButtonBuilderFactory buttonBuilderFactory) {
        this.shopQueryService = shopQueryService;
        this.shopProfileQueryService = shopProfileQueryService;
        this.menuBuilderFactory = menuBuilderFactory;
        this.buttonBuilderFactory = buttonBuilderFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BOOK, "Mini Pets");
        builder.addTooltip("Mini pets are cute little creatures");
        builder.addTooltip("that will follow and cheer you on");
        builder.addTooltip("wherever you go.");
        builder.addTooltip("");
        builder.addTooltip("You can unlock mini pets via the shop");
        builder.addTooltip("or a loot crate.");
        return builder.build();
    }

    Button createPetButton(MiniPetId miniPetId, ShopProfile shopProfile) {
        boolean hasUnlocked = shopProfile.hasMiniPet(miniPetId);
        MiniPet miniPet = shopQueryService.getMiniPet(miniPetId.toString());
        ButtonBuilder builder = buttonBuilderFactory.createSkullButtonBuilder(miniPet.getSkull(), miniPet.getName());
        miniPet.getLore().forEach(builder::addTooltip);
        builder.addTooltip("");
        if (hasUnlocked) {
            builder.addTooltip(GREEN + "[CLICK] To summon");
        } else {
            builder.addTooltip(ORANGE + "You haven't unlocked this mini yet");
        }

        builder.withAction(action -> {
            if (hasUnlocked) {
                // TODO petCommandService.summonPet(MiniPet pet, playerId);
            }
        });
        return builder.build();
    }

    @Override
    public Menu createMiniPetsMenu(UUID playerId) {
        ShopProfile shopProfile = shopProfileQueryService.getProfile(playerId);
        MenuBuilder builder = menuBuilderFactory.createMenuBuilder("Mini Pets", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());
        builder.addButton(2, createPetButton(MiniPetId.COW, shopProfile));
        builder.addButton(3, createPetButton(MiniPetId.CHICKEN, shopProfile));
        builder.addButton(4, createPetButton(MiniPetId.PIG, shopProfile));
        builder.addButton(5, createPetButton(MiniPetId.SHEEP, shopProfile));
        return builder.build();
    }
}
