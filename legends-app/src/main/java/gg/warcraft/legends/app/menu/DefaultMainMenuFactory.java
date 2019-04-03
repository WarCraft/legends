package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnCommandService;
import gg.warcraft.legends.api.core.service.SpawnQueryService;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.ChallengesMenuFactory;
import gg.warcraft.legends.api.menu.CollectablesMenuFactory;
import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.legends.api.menu.HelpMenuFactory;
import gg.warcraft.legends.api.menu.LeaderboardsMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.ShopMenuFactory;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
import gg.warcraft.legends.api.menu.TeamMenuFactory;
import gg.warcraft.legends.app.entity.player.rank.handler.PrestigeAttributesHandler;
import gg.warcraft.legends.app.menu.task.ItemClaimTask;
import gg.warcraft.monolith.api.command.service.CommandCommandService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.attribute.Attributes;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.item.service.ItemStorageCommandService;
import gg.warcraft.monolith.api.item.service.ItemStorageQueryService;
import gg.warcraft.monolith.api.menu.Button;
import gg.warcraft.monolith.api.menu.ButtonBuilder;
import gg.warcraft.monolith.api.menu.ButtonBuilderFactory;
import gg.warcraft.monolith.api.menu.Menu;
import gg.warcraft.monolith.api.menu.MenuBuilder;
import gg.warcraft.monolith.api.menu.MenuBuilderFactory;
import gg.warcraft.monolith.api.menu.MenuSize;
import gg.warcraft.monolith.api.menu.service.MenuCommandService;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.api.util.StringUtils;
import gg.warcraft.monolith.api.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultMainMenuFactory implements MainMenuFactory {
    private static final String YELLOW = ColorCode.YELLOW.toString();
    private static final String GRAY = ColorCode.GRAY.toString();

    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final CommandCommandService commandCommandService;
    private final MenuCommandService menuCommandService;
    private final SpawnQueryService spawnQueryService;
    private final SpawnCommandService spawnCommandService;
    private final ItemStorageQueryService itemStorageQueryService;
    private final ItemStorageCommandService itemStorageCommandService;
    private final MenuBuilderFactory menuBuilderFactory;
    private final ButtonBuilderFactory buttonBuilderFactory;
    private final SpecializationMenuFactory specializationMenuFactory;
    private final TeamMenuFactory teamMenuFactory;
    private final DailyLoginMenuFactory dailyLoginMenuFactory;
    private final CollectablesMenuFactory collectablesMenuFactory;
    private final ChallengesMenuFactory challengesMenuFactory;
    private final LeaderboardsMenuFactory leaderboardsMenuFactory;
    private final ShopMenuFactory shopMenuFactory;
    private final HelpMenuFactory helpMenuFactory;
    private final StringUtils stringUtils;
    private final TimeUtils timeUtils;
    private final TaskService taskService;

    @Inject
    public DefaultMainMenuFactory(LegendsPlayerQueryService playerQueryService,
                                  LegendsPlayerCommandService playerCommandService,
                                  CommandCommandService commandCommandService,
                                  MenuCommandService menuCommandService,
                                  SpawnQueryService spawnQueryService,
                                  SpawnCommandService spawnCommandService,
                                  MenuBuilderFactory menuBuilderFactory,
                                  ItemStorageCommandService itemStorageCommandService,
                                  ItemStorageQueryService itemStorageQueryService,
                                  ButtonBuilderFactory buttonBuilderFactory,
                                  SpecializationMenuFactory specializationMenuFactory,
                                  DailyLoginMenuFactory dailyLoginMenuFactory, TeamMenuFactory teamMenuFactory,
                                  CollectablesMenuFactory collectablesMenuFactory,
                                  LeaderboardsMenuFactory leaderboardsMenuFactory,
                                  ChallengesMenuFactory challengesMenuFactory, ShopMenuFactory shopMenuFactory,
                                  HelpMenuFactory helpMenuFactory, StringUtils stringUtils, TaskService taskService,
                                  TimeUtils timeUtils) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.commandCommandService = commandCommandService;
        this.menuCommandService = menuCommandService;
        this.spawnCommandService = spawnCommandService;
        this.spawnQueryService = spawnQueryService;
        this.menuBuilderFactory = menuBuilderFactory;
        this.itemStorageCommandService = itemStorageCommandService;
        this.itemStorageQueryService = itemStorageQueryService;
        this.buttonBuilderFactory = buttonBuilderFactory;
        this.specializationMenuFactory = specializationMenuFactory;
        this.dailyLoginMenuFactory = dailyLoginMenuFactory;
        this.teamMenuFactory = teamMenuFactory;
        this.collectablesMenuFactory = collectablesMenuFactory;
        this.challengesMenuFactory = challengesMenuFactory;
        this.leaderboardsMenuFactory = leaderboardsMenuFactory;
        this.shopMenuFactory = shopMenuFactory;
        this.helpMenuFactory = helpMenuFactory;
        this.stringUtils = stringUtils;
        this.taskService = taskService;
        this.timeUtils = timeUtils;
    }

    List<String> createAttributesView(LegendsPlayer player) {
        Attributes attributes = player.getAttributes();
        List<String> attributesView = new ArrayList<>();
        attributesView.add("Attributes:");
        int strength = (int) attributes.getValue(LegendsAttribute.STRENGTH);
        if (strength != 0) {
            attributesView.add(YELLOW + strength + GRAY + " Strength");
        }
        int dexterity = (int) attributes.getValue(LegendsAttribute.DEXTERITY);
        if (dexterity != 0) {
            attributesView.add(YELLOW + dexterity + GRAY + " Dexterity");
        }
        int intelligence = (int) attributes.getValue(LegendsAttribute.INTELLIGENCE);
        if (intelligence != 0) {
            attributesView.add(YELLOW + intelligence + GRAY + " Intelligence");
        }
        int vitality = (int) attributes.getValue(LegendsAttribute.VITALITY);
        if (vitality != 0) {
            attributesView.add(YELLOW + vitality + GRAY + " Vitality");
        }
        int crit = (int) attributes.getValue(LegendsAttribute.CRIT);
        if (crit != 0) {
            attributesView.add(YELLOW + crit + GRAY + " Crit");
        }
        return attributesView;
    }

    List<String> createCurrenciesView(LegendsPlayer player) {
        List<String> currenciesView = new ArrayList<>();
        currenciesView.add("Currencies:");
        int gold = player.getGold();
        currenciesView.add(YELLOW + gold + " Gold");
        int favor = player.getFavor();
        currenciesView.add(ColorCode.AQUA + "" + favor + " Favor");
        int fragments = player.getFragments();
        currenciesView.add(ColorCode.GOLD + "" + fragments + " Fragments");
        return currenciesView;
    }

    Button createPlayerButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        String playerName = player.getName();
        ButtonBuilder builder = buttonBuilderFactory.createSkullButtonBuilder(playerName, playerName);
        createAttributesView(player).forEach(builder::addTooltip);
        builder.addTooltip("");
        createCurrenciesView(player).forEach(builder::addTooltip);

        // TODO make sure that rank rewards are always stored in the item storage and then remove from rank impl
        if (player.getRankUpRewardsClaimed() < player.getRank().getLevel()) {
            if (!(player.getRankUpRewardsClaimed() == 0 && player.getRank().getLevel() == 1)) {
                builder.addTooltip("");
                builder.addTooltip(ColorCode.GREEN + "[SHIFT RIGHT CLICK] To claim your rank up rewards");
            }
        }

        builder.withAction(action -> {
            if (action.isShiftRightClick()) {
                commandCommandService.dispatchCommandFor("claimrankuprewards", playerId);
                menuCommandService.closeMenu(playerId);
            } else if (action.isLeftClick()) {
                Menu menu = specializationMenuFactory.createSpecializationMenu(playerId);
                menuCommandService.showMenu(menu, playerId);
            } else if (action.isRightClick()) {
                Menu menu = teamMenuFactory.createTeamMenu(playerId);
                menuCommandService.showMenu(menu, playerId);
            }
        });
        return builder.build();
    }

    List<String> createRankPassivesView(int level) {
        int prestige = level / 50; // NOTE this is hacky, 49/50 is the prestige level
        int clippedLevel = level % 50; // NOTE this is hacky, 49/50 is the prestige level
        int attributeValue = PrestigeAttributesHandler.attributeModifierValues.get(clippedLevel / 7);

        List<String> passivesView = new ArrayList<>();
        if (prestige == 0) {
            if (attributeValue > 0) {
                passivesView.add(ColorCode.YELLOW + "+" + attributeValue + " Vitality");
            }
        } else if (prestige == 1) {
            passivesView.add(ColorCode.YELLOW + "+10 Vitality");
            if (attributeValue > 0) {
                passivesView.add(ColorCode.YELLOW + "+" + attributeValue + " Strength");
                passivesView.add(ColorCode.YELLOW + "+" + attributeValue + " Dexterity");
                passivesView.add(ColorCode.YELLOW + "+" + attributeValue + " Intelligence");
            }
        } else if (prestige == 2) {
            passivesView.add(ColorCode.YELLOW + "+10 Vitality");
            passivesView.add(ColorCode.YELLOW + "+10 Strength");
            passivesView.add(ColorCode.YELLOW + "+10 Dexterity");
            passivesView.add(ColorCode.YELLOW + "+10 Intelligence");
            if (attributeValue > 0) {
                passivesView.add(ColorCode.YELLOW + "+" + attributeValue + " Crit");
            }
        } else if (prestige > 2) {
            passivesView.add(ColorCode.YELLOW + "+10 Vitality");
            passivesView.add(ColorCode.YELLOW + "+10 Strength");
            passivesView.add(ColorCode.YELLOW + "+10 Dexterity");
            passivesView.add(ColorCode.YELLOW + "+10 Intelligence");
            passivesView.add(ColorCode.YELLOW + "+10 Crit");
        }
        return passivesView;
    }

    Button createRankButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        String rank = player.getRank().getFormattedName();
        int level = player.getRank().getLevel();
        int levelUpPercentage = player.getLifetimeFavor() / player.getRank().getRequiredFavor();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.DIAMOND_SWORD, "Level & Rank");
        builder.addTooltip("Rank text");
        builder.addTooltip("");
        builder.addTooltip(ColorCode.WHITE + "Current level:");
        builder.addTooltip(ColorCode.YELLOW + "Lv. " + level + " (" + levelUpPercentage + "%)");
        builder.addTooltip("");
        builder.addTooltip(ColorCode.WHITE + "Current Rank:");
        builder.addTooltip(rank);
        builder.addTooltip("");
        builder.addTooltip(ColorCode.WHITE + "Active passives:");
        createRankPassivesView(level).forEach(builder::addTooltip);
        return builder.build();
    }

    List<String> createAbilitiesView(String specialization) {
        List<String> abilitiesView = new ArrayList<>();
        switch (specialization) {
            case "Armsmaster":
                abilitiesView.add(ColorCode.YELLOW + "[LEFT CLICK] Melee");
                abilitiesView.add(ColorCode.YELLOW + "[RIGHT CLICK] Hamstring");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT LEFT CLICK] Slam");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT RIGHT CLICK] Stand Your Ground");
                return abilitiesView;
            case "Berserker":
                abilitiesView.add(ColorCode.YELLOW + "[LEFT CLICK] Melee");
                abilitiesView.add(ColorCode.YELLOW + "[RIGHT CLICK] Maul");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT LEFT CLICK] Grudge");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT RIGHT CLICK] Shatter");
                return abilitiesView;
            case "Hunter":
                abilitiesView.add(ColorCode.YELLOW + "[LEFT CLICK] Toxic Arrow");
                abilitiesView.add(ColorCode.YELLOW + "[RIGHT CLICK] Shoot");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT LEFT CLICK] Trap");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT RIGHT CLICK] Disengage");
                return abilitiesView;
            case "Marksman":
                abilitiesView.add(ColorCode.YELLOW + "[LEFT CLICK] Power Shot");
                abilitiesView.add(ColorCode.YELLOW + "[RIGHT CLICK] Shoot");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT LEFT CLICK] Focus");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT RIGHT CLICK] Sneak");
                return abilitiesView;
            case "Tempest":
                abilitiesView.add(ColorCode.YELLOW + "[LEFT CLICK] Fireball");
                abilitiesView.add(ColorCode.YELLOW + "[RIGHT CLICK] Bolt");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT LEFT CLICK] Hail Blast");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT RIGHT CLICK] Molten Storm");
                return abilitiesView;
            case "Wizard":
                abilitiesView.add(ColorCode.YELLOW + "[LEFT CLICK] Fireball");
                abilitiesView.add(ColorCode.YELLOW + "[RIGHT CLICK] Arcane Missile");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT LEFT CLICK] Teleport");
                abilitiesView.add(ColorCode.YELLOW + "[SHIFT RIGHT CLICK] Mass Bamboozle");
                return abilitiesView;
            default:
                abilitiesView.add("Unknown");
                return abilitiesView;
        }
    }

    Button createClassButton(LegendsPlayer player) {
        String specialization = player.getSpecialization() != null
                ? player.getSpecialization().getName()
                : "Wayfarer";
        String specializationPrefix = stringUtils.isVowel(specialization.charAt(0)) ? "an " : "a ";
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.SHIELD, "Class");
        builder.addTooltip("You are currently fighting as " + specializationPrefix + YELLOW + specialization);
        builder.addTooltip("");
        builder.addTooltip(ColorCode.WHITE + "Abilities:");
        createAbilitiesView(specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view all classes");
        return builder.build();
    }

    List<String> createRacialPassivesView(String team, String specialization) {
        List<String> passivesView = new ArrayList<>();
        switch (team) {
            case "Humans":
                switch (specialization) {
                    case "Armsmaster":
                        passivesView.add(ColorCode.YELLOW + "Last Stand");
                        return passivesView;
                    case "Berserker":
                        passivesView.add(ColorCode.YELLOW + "Last Stand");
                        return passivesView;
                    case "Hunter":
                        passivesView.add(ColorCode.YELLOW + "Wolf Pet");
                        return passivesView;
                    case "Marksman":
                        passivesView.add(ColorCode.YELLOW + "Wolf Pet");
                        return passivesView;
                    case "Tempest":
                        passivesView.add(ColorCode.YELLOW + "Barrier");
                        return passivesView;
                    case "Wizard":
                        passivesView.add(ColorCode.YELLOW + "Barrier");
                        return passivesView;
                    default:
                        passivesView.add("Unknown");
                        return passivesView;
                }
            case "Orcs":
                switch (specialization) {
                    case "Armsmaster":
                        passivesView.add(ColorCode.YELLOW + "Bloodlust");
                        return passivesView;
                    case "Berserker":
                        passivesView.add(ColorCode.YELLOW + "Bloodlust");
                        return passivesView;
                    case "Hunter":
                        passivesView.add(ColorCode.YELLOW + "Pig Pet");
                        return passivesView;
                    case "Marksman":
                        passivesView.add(ColorCode.YELLOW + "Pig Pet");
                        return passivesView;
                    case "Tempest":
                        passivesView.add(ColorCode.YELLOW + "Flame Totem");
                        return passivesView;
                    case "Wizard":
                        passivesView.add(ColorCode.YELLOW + "Flame Totem");
                        return passivesView;
                    default:
                        passivesView.add("Unknown");
                        return passivesView;
                }
            default:
                passivesView.add("Unknown");
                return passivesView;
        }
    }

    Button createRaceButton(LegendsPlayer player) {
        String team = player.getTeam() != null
                ? player.getTeam().getPlural()
                : "Wayfarers";
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.SHIELD, "Race");
        builder.addTooltip("You are currently fighting for the " + YELLOW + team);
        builder.addTooltip("");
        builder.addTooltip(ColorCode.WHITE + "Racial passive:");
        String specialization = player.getSpecialization() != null
                ? player.getSpecialization().getName()
                : "Wayfarer";
        createRacialPassivesView(team, specialization).forEach(builder::addTooltip);
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view all races");
        return builder.build();
    }

    Button createDailyLoginButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.CAKE, "Daily Login");
        builder.addTooltip("Every day you get a reward just for logging");
        builder.addTooltip("in to the server! The more often you log in,");
        builder.addTooltip("the more interesting the rewards get. And do");
        builder.addTooltip("not worry; missing a day or two does not reset");
        builder.addTooltip("your progress.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view your rewards");

        builder.withAction(action -> {
            Menu menu = dailyLoginMenuFactory.createDailyLoginMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createDailyVoteButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.CAKE, "Daily Votes");
        builder.addTooltip("Votes text placeholder");
        builder.addTooltip("");
        builder.addTooltip("Websites:");
        builder.addTooltip(ColorCode.YELLOW + "http://link1");
        builder.addTooltip(ColorCode.YELLOW + "http://link2");
        builder.addTooltip(ColorCode.YELLOW + "http://link3");
        builder.addTooltip(ColorCode.YELLOW + "http://link4");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To paste these links to your chat");

        builder.withAction(action -> {
            Menu menu = dailyLoginMenuFactory.createDailyLoginMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createChallengesButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.TNT, "Challenges");
        builder.addTooltip("Challenges are a simple way of adding a");
        builder.addTooltip("little bit of extra action to your day to");
        builder.addTooltip("day activities on the server. Can you");
        builder.addTooltip("complete them all?");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view your challenges");

        builder.withAction(action -> {
            Menu menu = challengesMenuFactory.createChallengesMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createCollectablesButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BOOKSHELF, "Collectables");
        builder.addTooltip("From housing decorations to mini pets");
        builder.addTooltip("and more. There's a lot to collect on");
        builder.addTooltip("the server. Gotta collect 'em all!");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view your collection");

        builder.withAction(action -> {
            Menu menu = collectablesMenuFactory.createCollectablesMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createShopButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.ENCHANTED_GOLD_APPLE, "WarCraft: Shop");
        builder.addTooltip("To go even further beyond in supporting the");
        builder.addTooltip("server the WarCraft: Shop has ten different");
        builder.addTooltip("items for sale every day. These items can");
        builder.addTooltip("be bought with Fragments which are available");
        builder.addTooltip("from the web shop at " + YELLOW + "shop.warcraft.gg");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse the shop");

        builder.withAction(action -> {
            Menu menu = shopMenuFactory.createShopMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createLeaderboardsButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BANNER, "Leaderboards");
        builder.addTooltip("Your actions on the server do not go unnoticed.");
        builder.addTooltip("The Titans keep track of all the greatest warriors");
        builder.addTooltip("and reward those who finish first, second, or third");
        builder.addTooltip("on any of the daily, war, and all-time leaderboard!");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view the leaderboards");

        builder.withAction(action -> {
            Menu menu = leaderboardsMenuFactory.createLeaderboardsMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createItemStorageButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.CHEST, "Item Storage");
        builder.addTooltip("It is possible to recieve rewards while offline,");
        builder.addTooltip("such as winning a daily leaderboard. These items");
        builder.addTooltip("can be claimed from this menu at any time.");
        builder.addTooltip("");

        List<Item> storedItems = itemStorageQueryService.getStoredItems(playerId);
        if (storedItems.isEmpty()) {
            builder.addTooltip("There are no items currently waiting for you");
        } else {
            builder.addTooltip("You have " + storedItems.size() + " items waiting for you.");
            builder.addTooltip("[CLICK] To receive your items");

            ItemClaimTask itemClaimTask = new ItemClaimTask(itemStorageQueryService,
                    itemStorageCommandService, playerQueryService, playerCommandService,
                    taskService, playerId);
            taskService.runNextTick(itemClaimTask);

            menuCommandService.closeMenu(playerId);
        }

        return builder.build();
    }

    Button createReturnButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.FLINT, "Monolith Shard");
        builder.addTooltip("A pitch black splinter that is said to have");
        builder.addTooltip("been part of The Monolith. It can take you");
        builder.addTooltip("back to where The Monolith's power was once");
        builder.addTooltip("at its strongest.");
        builder.addTooltip("");

        long cooldown = spawnQueryService.getCooldown(playerId);
        long now = System.currentTimeMillis();
        if (cooldown >= now) {
            String timeToGo = timeUtils.getTimeToGoUntil(cooldown);
            builder.addTooltip(ColorCode.RED + timeToGo + " cooldown remaining");
        } else {
            builder.addTooltip(ColorCode.GREEN + "[CLICK] To warp to your spawn");
        }

        builder.withAction(action -> {
            long updatedCooldown = spawnQueryService.getCooldown(playerId);
            if (updatedCooldown < System.currentTimeMillis()) {
                spawnCommandService.warp(playerId);
                menuCommandService.closeMenu(playerId);
            }
        });
        return builder.build();
    }

    Button createHelpButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BOOK, "Help Menu");
        builder.addTooltip("Read the Server Manual or contact a member");
        builder.addTooltip("of staff if you have any more questions!");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To open the Help Menu");

        builder.withAction(action -> {
            Menu menu = helpMenuFactory.createHelpMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createMainMenu(UUID playerId) {
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        MenuBuilder builder = menuBuilderFactory.createMenuBuilder("WarCraft: Legends", MenuSize.TWO_ROWS);
        builder.addButton(0, createPlayerButton(player));
        builder.addButton(1, createRankButton(player));
        builder.addButton(2, createClassButton(player));
        builder.addButton(3, createRaceButton(player));

        builder.addButton(5, createItemStorageButton(player));
        builder.addButton(6, createReturnButton(player));

        builder.addButton(8, createHelpButton(player));

        builder.addButton(9, createDailyLoginButton(player));
        builder.addButton(10, createDailyVoteButton(player));
        builder.addButton(11, createChallengesButton(player));

        builder.addButton(13, createCollectablesButton(player));
        builder.addButton(14, createLeaderboardsButton(player));

        builder.addButton(16, createShopButton(player));
        return builder.build();
    }
}
