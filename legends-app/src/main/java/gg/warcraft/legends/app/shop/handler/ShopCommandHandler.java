package gg.warcraft.legends.app.shop.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.item.crate.LootCrateBuilder;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.Item;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class ShopCommandHandler implements CommandHandler {
    private final ShopProfileCommandService shopProfileCommandService;
    private final PlayerCommandService playerCommandService;
    private final LootCrateBuilderFactory lootCrateBuilderFactory;
    private final Logger pluginLogger;

    @Inject
    public ShopCommandHandler(ShopProfileCommandService shopProfileCommandService,
                              PlayerCommandService playerCommandService,
                              LootCrateBuilderFactory lootCrateBuilderFactory,
                              @PluginLogger Logger pluginLogger) {
        this.shopProfileCommandService = shopProfileCommandService;
        this.playerCommandService = playerCommandService;
        this.lootCrateBuilderFactory = lootCrateBuilderFactory;
        this.pluginLogger = pluginLogger;
    }

    private boolean handleFragments(String amount, String playerId) {
        try {
            int actualAmount = Integer.parseInt(amount);
            UUID actualPlayerId = UUID.fromString(playerId);
            if (actualAmount < 0) {
                pluginLogger.warning("Fragments purchase of " + -actualAmount + " got removed from " + playerId);
                playerCommandService.revokeCurrency(actualPlayerId, LegendsCurrency.FRAGMENTS, actualAmount);
            } else {
                playerCommandService.addCurrency(actualPlayerId, LegendsCurrency.FRAGMENTS, actualAmount);
                if (actualAmount == 5000) {
                    shopProfileCommandService.unlockPromotion(PromotionId.FIVE_YEAR_OLD_CAKE, UUID.fromString(playerId), false);
                }
            }
            return true;
        } catch (Exception ex) {
            pluginLogger.severe("Failed to handle fragments purchase of " + amount + " for " + playerId);
            return false;
        }
    }

    private boolean handleCrates(String amount, String playerId) {
        try {
            int actualAmount = Integer.parseInt(amount);
            UUID actualPlayerId = UUID.fromString(playerId);
            if (actualAmount < 0) {
                pluginLogger.warning("Crates purchase of " + -actualAmount + " got removed from " + playerId);
                // TODO how to revoke?
            } else {
                LootCrateBuilder crateBuilder = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.TITAN_CRATE);
                Item crate = crateBuilder.build().withStackSize(actualAmount);
                playerCommandService.giveItem(actualPlayerId, crate, false);
            }
            return true;
        } catch (Exception ex) {
            pluginLogger.severe("Failed to handle crates purchase of " + amount + " for " + playerId);
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isConsole()) {
            return false;
        }

        if (arguments.isEmpty()) {
            return false;
        }

        switch (arguments.get(0)) {
            case "fragments":
                if (arguments.size() != 3) {
                    return false;
                }
                return handleFragments(arguments.get(1), arguments.get(2));
            case "crates":
                if (arguments.size() != 3) {
                    return false;
                }
                return handleCrates(arguments.get(1), arguments.get(2));
        }
        return false;
    }
}
