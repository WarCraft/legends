package gg.warcraft.legends.app.core.handler;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.service.SpecializationCommandService;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.core.AuthorizationService;
import gg.warcraft.monolith.api.entity.player.Currency;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;

import java.util.List;
import java.util.UUID;

public class FishCommandHandler implements CommandHandler {
    private final AuthorizationService authorizationService;
    private final PlayerCommandService playerCommandService;
    private final TeamCommandService teamCommandService;
    private final SpecializationCommandService specializationCommandService;

    @Inject
    public FishCommandHandler(AuthorizationService authorizationService, PlayerCommandService playerCommandService,
                              TeamCommandService teamCommandService,
                              SpecializationCommandService specializationCommandService) {
        this.authorizationService = authorizationService;
        this.playerCommandService = playerCommandService;
        this.teamCommandService = teamCommandService;
        this.specializationCommandService = specializationCommandService;
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
            case "gold":
                return onCurrencyCommand(LegendsCurrency.GOLD, subArguments);
            case "favor":
                return onCurrencyCommand(LegendsCurrency.FAVOR, subArguments);
            case "fragments":
                return onCurrencyCommand(LegendsCurrency.FRAGMENTS, subArguments);
            default:
                return false;
        }
    }
}
