package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.leaderboard.service.LeaderboardQueryService;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsStatistic;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.LeaderboardsMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.monolith.api.entity.player.Player;
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

import java.util.Map;
import java.util.UUID;

public class DefaultLeaderboardsMenuFactory extends AbstractMenuFactory implements LeaderboardsMenuFactory {
    private final LegendsPlayerQueryService playerQueryService;
    private final LeaderboardQueryService leaderboardQueryService;
    private final MainMenuFactory mainMenuFactory;
    private final LeaderboardsMenuFactory leaderboardsMenuFactory;

    @Inject
    public DefaultLeaderboardsMenuFactory(LegendsPlayerQueryService playerQueryService,
                                          MenuCommandService menuCommandService,
                                          LeaderboardQueryService leaderboardQueryService,
                                          MenuBuilderFactory menuBuilderFactory,
                                          ButtonBuilderFactory buttonBuilderFactory,
                                          MainMenuFactory mainMenuFactory,
                                          LeaderboardsMenuFactory leaderboardsMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.playerQueryService = playerQueryService;
        this.leaderboardQueryService = leaderboardQueryService;
        this.leaderboardsMenuFactory = leaderboardsMenuFactory;
        this.mainMenuFactory = mainMenuFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Leaderboards");
        builder.addTooltip("Your actions on the server do not go unnoticed.");
        builder.addTooltip("The Titans keep track of all the greatest warriors");
        builder.addTooltip("and reward those who finish first, second, or third");
        builder.addTooltip("on any of the daily, war, and all-time leaderboard!");
        return builder.build();
    }

    Button createDailyLeaderboardsButton(LegendsPlayer player) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Daily Leaderboards");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view the daily leaderboards");

        builder.withAction(action -> {
            Menu menu = leaderboardsMenuFactory.createDailyLeaderboardsMenu(player.getId());
            showMenu(menu, player.getId());
        });
        return builder.build();
    }

    Button createWarLeaderboardsButton(LegendsPlayer player) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "War Leaderboards");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view the war leaderboards");

        builder.withAction(action -> {
            Menu menu = leaderboardsMenuFactory.createWarLeaderboardsMenu(player.getId());
            showMenu(menu, player.getId());
        });
        return builder.build();
    }

    Button createAlltimeLeaderboardsButton(LegendsPlayer player) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "All-time Leaderboards");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view the all-time leaderboards");

        builder.withAction(action -> {
            Menu menu = leaderboardsMenuFactory.createAlltimeLeaderboardsMenu(player.getId());
            showMenu(menu, player.getId());
        });
        return builder.build();
    }

    @Override
    public Menu createLeaderboardsMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = createMenuBuilder("Leaderboards", MenuSize.ONE_ROW);

        builder.addButton(0, createInfoButton());
        builder.addButton(2, createDailyLeaderboardsButton(player));
        builder.addButton(3, createWarLeaderboardsButton(player));
        builder.addButton(4, createAlltimeLeaderboardsButton(player));
        builder.addButton(8, createBackButton(playerId, mainMenuFactory::createMainMenu));

        return builder.build();
    }

    Button createDailyInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Daily Leaderboards");

        return builder.build();
    }

    Button createWarInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "War Leaderboards");

        return builder.build();
    }

    Button createAlltimeInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "All-time Leaderboards");

        return builder.build();
    }

    Button createLeaderboardButton(LegendsStatistic statistic, LegendsPlayer player) {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BANNER, statistic.getName());

        Map<UUID, Integer> leaders = leaderboardQueryService.getLeaders(statistic, 10);
        int position = 1;
        for (Map.Entry<UUID, Integer> leader : leaders.entrySet()) {
            Player leaderPlayer = playerQueryService.getPlayer(leader.getKey());
            builder.addTooltip(ColorCode.YELLOW.toString() + position + "." +
                    " " + leaderPlayer.getName() + " (" + leader.getValue() + ")");
            position += 1;
        }

        if (!leaders.keySet().contains(player.getId())) {
            builder.addTooltip("");
            builder.addTooltip("??. " + player.getName() + " (" + player.getStatistic(statistic.getKey()) + ")");
        }

        return builder.build();
    }

    @Override
    public Menu createDailyLeaderboardsMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = createMenuBuilder("Daily Leaderboards", MenuSize.TWO_ROWS);

        builder.addButton(0, createDailyInfoButton());
        builder.addButton(2, createLeaderboardButton(LegendsStatistic.DAILY_KILLS, player));
        builder.addButton(3, createLeaderboardButton(LegendsStatistic.DAILY_DAMAGE_DONE, player));
        builder.addButton(4, createLeaderboardButton(LegendsStatistic.DAILY_MONSTER_KILLS, player));
        builder.addButton(5, createLeaderboardButton(LegendsStatistic.DAILY_RESOURCES_GATHERED, player));
        builder.addButton(6, createLeaderboardButton(LegendsStatistic.DAILY_TIME_PLAYED, player));
        builder.addButton(7, createLeaderboardButton(LegendsStatistic.DAILY_FAVOR_GAINED, player));
        builder.addButton(8, createLeaderboardButton(LegendsStatistic.DAILY_GOLD_GAINED, player));
        builder.addButton(11, createLeaderboardButton(LegendsStatistic.DAILY_CAMPS_CAPTURED, player));
        builder.addButton(12, createLeaderboardButton(LegendsStatistic.DAILY_CAMPS_DEFENDED, player));
        builder.addButton(13, createLeaderboardButton(LegendsStatistic.DAILY_OUTPOSTS_CAPTURED, player));
        builder.addButton(14, createLeaderboardButton(LegendsStatistic.DAILY_OUTPOSTS_DEFENDED, player));
        builder.addButton(15, createLeaderboardButton(LegendsStatistic.DAILY_KEEPS_CAPTURED, player));
        builder.addButton(16, createLeaderboardButton(LegendsStatistic.DAILY_KEEPS_DEFENDED, player));
        builder.addButton(17, createLeaderboardButton(LegendsStatistic.DAILY_CRATES_OPENED, player));
        builder.addButton(9, createBackButton(playerId, leaderboardsMenuFactory::createLeaderboardsMenu));

        return builder.build();
    }

    @Override
    public Menu createWarLeaderboardsMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = createMenuBuilder("War Leaderboards", MenuSize.TWO_ROWS);

        builder.addButton(0, createDailyInfoButton());
        builder.addButton(2, createLeaderboardButton(LegendsStatistic.WARRY_KILLS, player));
        builder.addButton(3, createLeaderboardButton(LegendsStatistic.WARRY_DAMAGE_DONE, player));
        builder.addButton(4, createLeaderboardButton(LegendsStatistic.WARRY_MONSTER_KILLS, player));
        builder.addButton(5, createLeaderboardButton(LegendsStatistic.WARRY_RESOURCES_GATHERED, player));
        builder.addButton(6, createLeaderboardButton(LegendsStatistic.WARRY_TIME_PLAYED, player));
        builder.addButton(7, createLeaderboardButton(LegendsStatistic.WARRY_FAVOR_GAINED, player));
        builder.addButton(8, createLeaderboardButton(LegendsStatistic.WARRY_GOLD_GAINED, player));
        builder.addButton(11, createLeaderboardButton(LegendsStatistic.WARRY_CAMPS_CAPTURED, player));
        builder.addButton(12, createLeaderboardButton(LegendsStatistic.WARRY_CAMPS_DEFENDED, player));
        builder.addButton(13, createLeaderboardButton(LegendsStatistic.WARRY_OUTPOSTS_CAPTURED, player));
        builder.addButton(14, createLeaderboardButton(LegendsStatistic.WARRY_OUTPOSTS_DEFENDED, player));
        builder.addButton(15, createLeaderboardButton(LegendsStatistic.WARRY_KEEPS_CAPTURED, player));
        builder.addButton(16, createLeaderboardButton(LegendsStatistic.WARRY_KEEPS_DEFENDED, player));
        builder.addButton(17, createLeaderboardButton(LegendsStatistic.WARRY_CRATES_OPENED, player));
        builder.addButton(9, createBackButton(playerId, leaderboardsMenuFactory::createLeaderboardsMenu));

        return builder.build();
    }

    @Override
    public Menu createAlltimeLeaderboardsMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = createMenuBuilder("All-time Leaderboards", MenuSize.TWO_ROWS);

        builder.addButton(0, createDailyInfoButton());
        builder.addButton(2, createLeaderboardButton(LegendsStatistic.ALLTIME_KILLS, player));
        builder.addButton(3, createLeaderboardButton(LegendsStatistic.ALLTIME_DAMAGE_DONE, player));
        builder.addButton(4, createLeaderboardButton(LegendsStatistic.ALLTIME_MONSTER_KILLS, player));
        builder.addButton(5, createLeaderboardButton(LegendsStatistic.ALLTIME_RESOURCES_GATHERED, player));
        builder.addButton(6, createLeaderboardButton(LegendsStatistic.ALLTIME_TIME_PLAYED, player));
        builder.addButton(7, createLeaderboardButton(LegendsStatistic.ALLTIME_FAVOR_GAINED, player));
        builder.addButton(8, createLeaderboardButton(LegendsStatistic.ALLTIME_GOLD_GAINED, player));
        builder.addButton(11, createLeaderboardButton(LegendsStatistic.ALLTIME_CAMPS_CAPTURED, player));
        builder.addButton(12, createLeaderboardButton(LegendsStatistic.ALLTIME_CAMPS_DEFENDED, player));
        builder.addButton(13, createLeaderboardButton(LegendsStatistic.ALLTIME_OUTPOSTS_CAPTURED, player));
        builder.addButton(14, createLeaderboardButton(LegendsStatistic.ALLTIME_OUTPOSTS_DEFENDED, player));
        builder.addButton(15, createLeaderboardButton(LegendsStatistic.ALLTIME_KEEPS_CAPTURED, player));
        builder.addButton(16, createLeaderboardButton(LegendsStatistic.ALLTIME_KEEPS_DEFENDED, player));
        builder.addButton(17, createLeaderboardButton(LegendsStatistic.ALLTIME_CRATES_OPENED, player));
        builder.addButton(9, createBackButton(playerId, leaderboardsMenuFactory::createLeaderboardsMenu));

        return builder.build();
    }
}
