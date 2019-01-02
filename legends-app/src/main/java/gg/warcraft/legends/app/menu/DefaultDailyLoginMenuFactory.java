package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.item.crate.LootCrateBuilderFactory;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

public class DefaultDailyLoginMenuFactory extends AbstractMenuFactory implements DailyLoginMenuFactory {
    private static final String DAILY_LOGIN_COUNTER_DATA = "dailyLoginCounter";
    private static final String DAILY_LOGIN_LAST_CLAIMED_DATA = "dailyLoginLastClaimed";
    private static final NavigableMap<Integer, Item> DAILY_REWARDS = new TreeMap<>();

    private final PlayerQueryService playerQueryService;
    private final PlayerCommandService playerCommandService;
    private final MenuCommandService menuCommandService;
    private final MainMenuFactory mainMenuFactory;
    private final Logger pluginLogger;

    @Inject
    public DefaultDailyLoginMenuFactory(PlayerQueryService playerQueryService,
                                        PlayerCommandService playerCommandService,
                                        MenuCommandService menuCommandService,
                                        MenuBuilderFactory menuBuilderFactory,
                                        ButtonBuilderFactory buttonBuilderFactory,
                                        MainMenuFactory mainMenuFactory,
                                        LootCrateBuilderFactory lootCrateBuilderFactory,
                                        @PluginLogger Logger pluginLogger) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.menuCommandService = menuCommandService;
        this.mainMenuFactory = mainMenuFactory;
        this.pluginLogger = pluginLogger;

        if (DAILY_REWARDS.isEmpty()) {
            Item warCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.WAR_CRATE).build();
            Item legendCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.LEGEND_CRATE).build();
            Item titanCrate = lootCrateBuilderFactory.createLootCrateBuilder(LootCrateType.TITAN_CRATE).build();

            DAILY_REWARDS.put(1, warCrate);
            DAILY_REWARDS.put(7, legendCrate);
            DAILY_REWARDS.put(8, warCrate);
            DAILY_REWARDS.put(14, legendCrate);
            DAILY_REWARDS.put(15, warCrate.withStackSize(2));
            DAILY_REWARDS.put(21, legendCrate.withStackSize(2));
            DAILY_REWARDS.put(22, warCrate.withStackSize(2));
            DAILY_REWARDS.put(28, titanCrate);
        }
    }

    int getClaimCounter(Player player) {
        String counterAsString = player.getData(DAILY_LOGIN_COUNTER_DATA);
        int counter;
        if (counterAsString == null) {
            counter = 0;
        } else {
            try {
                counter = Integer.parseInt(counterAsString);
            } catch (NumberFormatException ex) {
                pluginLogger.warning("Failed to create daily login menu for player " + player.getId() +
                        ", could not parse counter data.");
                return Integer.MAX_VALUE;
            }
        }
        return counter;
    }

    LocalDate getLastClaimed(Player player) {
        String lastClaimedAsString = player.getData(DAILY_LOGIN_LAST_CLAIMED_DATA);
        LocalDate lastClaimed;
        if (lastClaimedAsString == null) {
            lastClaimed = LocalDate.now().minusDays(1);
        } else {
            try {
                lastClaimed = LocalDate.parse(lastClaimedAsString);
            } catch (DateTimeParseException ex) {
                pluginLogger.warning("Failed to create daily login menu for player " + player.getId() +
                        ", could not parse last claimed data.");
                return LocalDate.now();
            }
        }
        return lastClaimed;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Daily Login");
        builder.addTooltip("Every day you can get a reward just for");
        builder.addTooltip("logging in! The more often you log in, the");
        builder.addTooltip("more interesting the rewards. And don't");
        builder.addTooltip("worry; missing a day or two does not reset");
        builder.addTooltip("your progress.");
        return builder.build();
    }

    Button createRewardButton(UUID playerId, int day, int claimed, boolean canClaim, boolean isSpecial) {
        boolean dayClaimed = day <= claimed;
        ItemType icon;
        if (isSpecial) {
            icon = dayClaimed ? ItemType.BLACK_STAINED_GLASS : ItemType.ENDER_CHEST;
        } else {
            icon = dayClaimed ? ItemType.BROWN_STAINED_GLASS : ItemType.CHEST;
        }

        LootCrateType lootCrateType;
        ButtonBuilder builder = createSimpleButtonBuilder(icon, "Day " + day);
        switch (day) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                builder.addTooltip("1x War Crate");
                lootCrateType = LootCrateType.WAR_CRATE;
                break;
            case 7:
            case 14:
                builder.addTooltip("1x Legend Crate");
                lootCrateType = LootCrateType.LEGEND_CRATE;
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
                builder.addTooltip("2x War Crate");
                lootCrateType = LootCrateType.WAR_CRATE;
                break;
            case 21:
                builder.addTooltip("2x Legend Crate");
                lootCrateType = LootCrateType.LEGEND_CRATE;
                break;
            case 28:
                builder.addTooltip("1x Titan Crate");
                lootCrateType = LootCrateType.TITAN_CRATE;
                break;
            default:
                builder.addTooltip("Unknown");
                lootCrateType = LootCrateType.WAR_CRATE;
                break;
        }
        builder.addTooltip("");

        if (canClaim) {
            builder.addTooltip(ColorCode.GREEN + "[CLICK] To claim today's reward!");

            builder.withAction(action -> {
                Player player = playerQueryService.getPlayer(playerId);
                int claimCounter = getClaimCounter(player);
                LocalDate lastClaimed = getLastClaimed(player);
                LocalDate now = LocalDate.now();
                if (lastClaimed.isBefore(now)) {
                    if (player.getInventory().getSpace() > 0) {
                        int newClaimCounter = claimCounter + 1;
                        playerCommandService.setData(playerId, DAILY_LOGIN_COUNTER_DATA, "" + newClaimCounter);
                        playerCommandService.setData(playerId, DAILY_LOGIN_LAST_CLAIMED_DATA, now.toString());

                        Item reward = DAILY_REWARDS.floorEntry(newClaimCounter).getValue();
                        playerCommandService.giveItem(playerId, reward, false);
                    } else {
                        playerCommandService.sendNotification(playerId, "Please make sure you have at least 1 free inventory slot.");
                    }
                }

                menuCommandService.closeMenu(playerId);
            });
        } else {
            builder.addTooltip(ColorCode.GOLD + "Come back tomorrow for another reward!");
        }
        return builder.build();
    }

    @Override
    public Menu createDailyLoginMenu(UUID playerId) {
        Player player = playerQueryService.getPlayer(playerId);
        int claimCounter = getClaimCounter(player);
        LocalDate lastClaimed = getLastClaimed(player);
        boolean canClaim = lastClaimed.isBefore(LocalDate.now());
        if (canClaim && claimCounter >= 28) {
            claimCounter = 0;
        }

        MenuBuilder builder = createMenuBuilder("Daily Login", MenuSize.FOUR_ROWS);
        builder.addButton(0, createInfoButton());
        builder.addButton(9, createBackButton(playerId, mainMenuFactory::createMainMenu));

        // Week 1
        builder.addButton(2, createRewardButton(playerId, 1, claimCounter, canClaim, false));
        builder.addButton(3, createRewardButton(playerId, 2, claimCounter, canClaim, false));
        builder.addButton(4, createRewardButton(playerId, 3, claimCounter, canClaim, false));
        builder.addButton(5, createRewardButton(playerId, 4, claimCounter, canClaim, false));
        builder.addButton(6, createRewardButton(playerId, 5, claimCounter, canClaim, false));
        builder.addButton(7, createRewardButton(playerId, 6, claimCounter, canClaim, false));
        builder.addButton(8, createRewardButton(playerId, 7, claimCounter, canClaim, true));

        // Week 2
        builder.addButton(11, createRewardButton(playerId, 8, claimCounter, canClaim, false));
        builder.addButton(12, createRewardButton(playerId, 9, claimCounter, canClaim, false));
        builder.addButton(13, createRewardButton(playerId, 10, claimCounter, canClaim, false));
        builder.addButton(14, createRewardButton(playerId, 11, claimCounter, canClaim, false));
        builder.addButton(15, createRewardButton(playerId, 12, claimCounter, canClaim, false));
        builder.addButton(16, createRewardButton(playerId, 13, claimCounter, canClaim, false));
        builder.addButton(17, createRewardButton(playerId, 14, claimCounter, canClaim, true));

        // Week 3
        builder.addButton(20, createRewardButton(playerId, 15, claimCounter, canClaim, false));
        builder.addButton(21, createRewardButton(playerId, 16, claimCounter, canClaim, false));
        builder.addButton(22, createRewardButton(playerId, 17, claimCounter, canClaim, false));
        builder.addButton(23, createRewardButton(playerId, 18, claimCounter, canClaim, false));
        builder.addButton(24, createRewardButton(playerId, 19, claimCounter, canClaim, false));
        builder.addButton(25, createRewardButton(playerId, 20, claimCounter, canClaim, false));
        builder.addButton(26, createRewardButton(playerId, 21, claimCounter, canClaim, true));

        // Week 4
        builder.addButton(29, createRewardButton(playerId, 22, claimCounter, canClaim, false));
        builder.addButton(30, createRewardButton(playerId, 23, claimCounter, canClaim, false));
        builder.addButton(31, createRewardButton(playerId, 24, claimCounter, canClaim, false));
        builder.addButton(32, createRewardButton(playerId, 25, claimCounter, canClaim, false));
        builder.addButton(33, createRewardButton(playerId, 26, claimCounter, canClaim, false));
        builder.addButton(34, createRewardButton(playerId, 27, claimCounter, canClaim, false));
        builder.addButton(35, createRewardButton(playerId, 28, claimCounter, canClaim, true));

        return builder.build();
    }
}
