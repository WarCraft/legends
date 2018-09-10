package gg.warcraft.legends.app.menu.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.LegendsMenuFactory;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;

import java.util.List;
import java.util.UUID;

public class MenuCommandHandler implements CommandHandler {
    private final MenuCommandService menuCommandService;
    private final LegendsMenuFactory menuFactory;

    @Inject
    public MenuCommandHandler(MenuCommandService menuCommandService, LegendsMenuFactory menuFactory) {
        this.menuCommandService = menuCommandService;
        this.menuFactory = menuFactory;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        switch (label) {
            case "me":
            case "menu":
            case "monolith":
            case "help":
                Menu mainMenu = menuFactory.createMainMenu(playerId);
                menuCommandService.showMenu(mainMenu, playerId);
                break;
            case "team":
            case "race":
                Menu teamMenu = menuFactory.createTeamMenu(playerId);
                menuCommandService.showMenu(teamMenu, playerId);
                break;
            case "spec":
            case "specialization":
            case "class":
            case "kits":
                Menu specializationMenu = menuFactory.createSpecializationMenu(playerId);
                menuCommandService.showMenu(specializationMenu, playerId);
                break;
            case "dec":
            case "decoration":
            case "decorations":
                Menu decorationsMenu = menuFactory.createDecorationsMenu(playerId);
                menuCommandService.showMenu(decorationsMenu, playerId);
                break;
            case "pet":
            case "pets":
            case "minipet":
            case "minipets":
            case "mp":
                Menu miniPetsMenu = menuFactory.createMiniPetsMenu(playerId);
                menuCommandService.showMenu(miniPetsMenu, playerId);
                break;
        }
        return true;
    }
}
