package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.TeamMenuFactory;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.ButtonBuilder;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuSize;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultTeamMenuFactory extends AbstractMenuFactory implements TeamMenuFactory {
    private static final String YELLOW = ColorCode.YELLOW.toString();
    private static final String GREEN = ColorCode.GREEN.toString();
    private static final String ORANGE = ColorCode.GOLD.toString();
    private static final String RED = ColorCode.RED.toString();

    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final PlayerCommandService playerCommandService;
    private final MainMenuFactory mainMenuFactory;

    @Inject
    public DefaultTeamMenuFactory(LegendsPlayerQueryService legendsPlayerQueryService,
                                  PlayerCommandService playerCommandService, MenuCommandService menuCommandService,
                                  MenuBuilderFactory menuBuilderFactory, ButtonBuilderFactory buttonBuilderFactory,
                                  MainMenuFactory mainMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.playerCommandService = playerCommandService;
        this.mainMenuFactory = mainMenuFactory;
    }

    List<String> createSwitchView(Team team, boolean canSwitch, String cooldown) {
        List<String> view = new ArrayList<>();
        if (canSwitch) {
            if (cooldown == null) {
                view.add(GREEN + "[SHIFT RIGHT CLICK] To switch to the " + team.getName());
            } else {
                view.add(RED + "You can change teams in " + cooldown);
            }
        } else {
            view.add("This is your current team");
        }
        return view;
    }

    Button createInfoButton(LegendsPlayer player) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Choosing a team");
        builder.addTooltip("You are currently fighting for the");
        builder.addTooltip(YELLOW + player.getTeam().getName());
        builder.addTooltip("");
//        builder.addTooltip("You can switch teams freely the first");
//        builder.addTooltip("3 days of a war (start of the month).");
//        builder.addTooltip("Alternatively, if you really want to");
//        builder.addTooltip("switch teams right now you can do so");
//        builder.addTooltip("for a few Fragments.");
//        builder.addTooltip("");
        if (true) {
            builder.addTooltip(GREEN + "Changing teams is currently free");
        } else {
            builder.addTooltip(ORANGE + "Changing teams at this time costs 800 Fragments");
        }
        return builder.build();
    }

    Button createHumanButton(UUID playerId, boolean canSwitch) {
        Team team = LegendsTeam.HUMAN;
        ButtonBuilder builder = createSkullButtonBuilder("eijlen", team.getName());
        // TODO add some human lore
        builder.addTooltip("");
        createSwitchView(team, canSwitch, null).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (canSwitch && action.isShiftRightClick()) {
                playerCommandService.setTeam(playerId, team);
            }
        });
        return builder.build();
    }

    Button createOrcButton(UUID playerId, boolean canSwitch) {
        Team team = LegendsTeam.ORC;
        ButtonBuilder builder = createSkullButtonBuilder("fishb6nes", team.getName());
        // TODO add some orc lore
        builder.addTooltip("");
        createSwitchView(team, canSwitch, null).forEach(builder::addTooltip);

        builder.withAction(action -> {
            if (canSwitch && action.isShiftRightClick()) {
                playerCommandService.setTeam(playerId, team);
            }
        });
        return builder.build();
    }

    @Override
    public Menu createTeamMenu(UUID playerId) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
        Team team = player.getTeam();
        MenuBuilder builder = createMenuBuilder("Team", MenuSize.ONE_ROW);
        builder.addButton(0, createInfoButton(player));
        boolean canSwitchHuman = team != LegendsTeam.HUMAN;
        builder.addButton(2, createHumanButton(playerId, canSwitchHuman));
        boolean canSwitchOrc = team != LegendsTeam.ORC;
        builder.addButton(3, createOrcButton(playerId, canSwitchOrc));
        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));
        return builder.build();
    }
}
