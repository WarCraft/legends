package gg.warcraft.legends.app.ai.npc.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.ai.npc.menu.BlueprintVendorDialogFactory;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.BlueprintBuilderFactory;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.Item;
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

public class DefaultBlueprintVendorDialogFactory extends AbstractMenuFactory implements BlueprintVendorDialogFactory {
    private static final String BLUE = ColorCode.AQUA.toString();

    private final LegendsPlayerQueryService playerQueryService;
    private final PlayerCommandService playerCommandService;
    private final BlueprintBuilderFactory blueprintBuilderFactory;

    @Inject
    public DefaultBlueprintVendorDialogFactory(MenuCommandService menuCommandService,
                                               LegendsPlayerQueryService playerQueryService,
                                               PlayerCommandService playerCommandService,
                                               MenuBuilderFactory menuBuilderFactory,
                                               ButtonBuilderFactory buttonBuilderFactory,
                                               BlueprintBuilderFactory blueprintBuilderFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.blueprintBuilderFactory = blueprintBuilderFactory;
    }

    private Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Blueprint Vendors");
        builder.addTooltip("Blueprints are schematics that allow");
        builder.addTooltip("you to place siege plots. Once placed");
        builder.addTooltip("a siege plot requires certain resources");
        builder.addTooltip("to build the weapon of the blueprint.");
        return builder.build();
    }

    private Button createBlueprintButton(UUID playerId) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.PAPER, "Trebuchet Blueprint");
        builder.addTooltip("This blueprint allows you to place a");
        builder.addTooltip("trebuchet plot. The trebuchet is a long");
        builder.addTooltip("distance siege weapon that can destroy");
        builder.addTooltip("enemy fortifications with ease.");
        builder.addTooltip("");
        builder.addTooltip(BLUE + "[SHIFT RIGHT CLICK] to purchase a trebuchet");
        builder.addTooltip(BLUE + "blueprint for 50 favor.");

        builder.withAction(click -> {
            if (click.isShiftRightClick()) {
                LegendsPlayer player = playerQueryService.getPlayer(playerId);
                if (player.getFavor() < 50) {
                    playerCommandService.sendNotification(playerId, "You need at least 50 favor to purchase this item.");
                    closeMenu(playerId);
                    return;
                }

                if (player.getInventory().getSpace() < 1) {
                    playerCommandService.sendNotification(playerId, "Please make sure you have at least 1 free inventory slot.");
                    closeMenu(playerId);
                    return;
                }

                playerCommandService.removeCurrency(playerId, LegendsCurrency.FAVOR, 50);
                Item blueprint = blueprintBuilderFactory
                        .createBlueprintBuilder("Trebuchet Blueprint", ItemRarity.COMMON)
                        .build();
                playerCommandService.giveItem(playerId, blueprint, false);
            }
        });
        return builder.build();
    }

    @Override
    public Menu createBlueprintVendorDialog(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("Blueprint Vendor", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton());
        builder.addButton(2, createBlueprintButton(playerId));
        return builder.build();
    }
}
