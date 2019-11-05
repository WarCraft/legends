package gg.warcraft.legends.app.core.handler;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.service.SpecializationCommandService;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.menu.AdminMenuFactory;
import gg.warcraft.legends.app.shop.handler.ShopCycleHandler;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.core.AuthorizationService;
import gg.warcraft.monolith.api.core.PersistenceCache;
import gg.warcraft.monolith.api.entity.player.Currency;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.world.block.backup.BlockBackup;
import gg.warcraft.monolith.api.world.block.backup.BlockBackupService;

import java.util.List;
import java.util.UUID;

public class FishCommandHandler implements CommandHandler {
    private final AuthorizationService authorizationService;
    private final PlayerCommandService playerCommandService;
    private final TeamCommandService teamCommandService;
    private final SpecializationCommandService specializationCommandService;
    private final BlockBackupService blockBackupService;
    private final MenuCommandService menuCommandService;
    private final ShopCycleHandler shopCycleHandler;
    private final AdminMenuFactory adminMenuFactory;
    private final PersistenceCache persistenceCache;

    @Inject
    public FishCommandHandler(AuthorizationService authorizationService, PlayerCommandService playerCommandService,
                              TeamCommandService teamCommandService,
                              SpecializationCommandService specializationCommandService,
                              BlockBackupService blockBackupService,
                              MenuCommandService menuCommandService,
                              ShopCycleHandler shopCycleHandler,
                              AdminMenuFactory adminMenuFactory, PersistenceCache persistenceCache) {
        this.authorizationService = authorizationService;
        this.playerCommandService = playerCommandService;
        this.teamCommandService = teamCommandService;
        this.specializationCommandService = specializationCommandService;
        this.blockBackupService = blockBackupService;
        this.menuCommandService = menuCommandService;
        this.shopCycleHandler = shopCycleHandler;
        this.adminMenuFactory = adminMenuFactory;
        this.persistenceCache = persistenceCache;
    }

    boolean onMenuCommand(UUID playerId) {
        Menu adminMenu = adminMenuFactory.createFishMenu(playerId);
        menuCommandService.showMenu(adminMenu, playerId);
        return true;
    }

    boolean onRestoreAllCommand() {
        // TODO this should delete any domain objects that are linked to temporary blocks like Siege Weapons
        blockBackupService.restoreBackups();
        return true;
    }

    boolean onCurrencyCommand(Currency currency, List<String> arguments) {
        if (arguments.size() != 3) {
            return false;
        }

        try {
            String action = arguments.get(0);
            int amount = Integer.parseInt(arguments.get(1));
            UUID playerId = UUID.fromString(arguments.get(2));
            switch (action) {
                case "add":
                    playerCommandService.addCurrency(playerId, currency, amount);
                    return true;
                case "remove":
                    playerCommandService.removeCurrency(playerId, currency, amount);
                    return true;
                case "revoke":
                    playerCommandService.revokeCurrency(playerId, currency, amount);
                    return true;
                default:
                    return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    boolean onClearCacheCommand(List<String> arguments) {
        if (arguments.size() > 1) {
            return false;
        }

        if (arguments.isEmpty()) {
            persistenceCache.clear();
        } else {
            persistenceCache.delete(arguments.get(0));
        }
        return true;
    }

    boolean onCycleShopCommand() {
        shopCycleHandler.onDailyTickEvent(null); // TODO move cycle method into command service
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (sender.isPlayer() && !authorizationService.isDev(sender.getPlayerId())) {
            return false;
        }

        if (arguments.isEmpty()) {
            return false;
        }

        List<String> subArguments = arguments.subList(1, arguments.size());
        switch (arguments.get(0)) {
            case "menu":
                return onMenuCommand(sender.getPlayerId());
            case "restoreall":
                return onRestoreAllCommand();
            case "gold":
                return onCurrencyCommand(LegendsCurrency.GOLD, subArguments);
            case "favor":
                return onCurrencyCommand(LegendsCurrency.FAVOR, subArguments);
            case "fragments":
                return onCurrencyCommand(LegendsCurrency.FRAGMENTS, subArguments);
            case "clearcache":
                return onClearCacheCommand(subArguments);
            case "cycleshop":
                return onCycleShopCommand();
            default:
                return false;
        }
    }
}
