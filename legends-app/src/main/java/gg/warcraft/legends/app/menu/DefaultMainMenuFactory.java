package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.ChallengesMenuFactory;
import gg.warcraft.legends.api.menu.CollectablesMenuFactory;
import gg.warcraft.legends.api.menu.DailyLoginMenuFactory;
import gg.warcraft.legends.api.menu.HelpMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.ShopMenuFactory;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
import gg.warcraft.legends.api.menu.TeamMenuFactory;
import gg.warcraft.monolith.api.command.service.CommandCommandService;
import gg.warcraft.monolith.api.entity.attribute.Attributes;
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
import gg.warcraft.monolith.api.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultMainMenuFactory implements MainMenuFactory {
    private static final String YELLOW = ColorCode.YELLOW.toString();
    private static final String GRAY = ColorCode.GRAY.toString();

    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final CommandCommandService commandCommandService;
    private final MenuCommandService menuCommandService;
    private final MenuBuilderFactory menuBuilderFactory;
    private final ButtonBuilderFactory buttonBuilderFactory;
    private final SpecializationMenuFactory specializationMenuFactory;
    private final TeamMenuFactory teamMenuFactory;
    private final DailyLoginMenuFactory dailyLoginMenuFactory;
    private final CollectablesMenuFactory collectablesMenuFactory;
    private final ChallengesMenuFactory challengesMenuFactory;
    private final ShopMenuFactory shopMenuFactory;
    private final HelpMenuFactory helpMenuFactory;
    private final StringUtils stringUtils;

    @Inject
    public DefaultMainMenuFactory(LegendsPlayerQueryService legendsPlayerQueryService,
                                  CommandCommandService commandCommandService,
                                  MenuCommandService menuCommandService, MenuBuilderFactory menuBuilderFactory,
                                  ButtonBuilderFactory buttonBuilderFactory,
                                  SpecializationMenuFactory specializationMenuFactory,
                                  DailyLoginMenuFactory dailyLoginMenuFactory, TeamMenuFactory teamMenuFactory,
                                  CollectablesMenuFactory collectablesMenuFactory,
                                  ChallengesMenuFactory challengesMenuFactory, ShopMenuFactory shopMenuFactory,
                                  HelpMenuFactory helpMenuFactory, StringUtils stringUtils) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.commandCommandService = commandCommandService;
        this.menuCommandService = menuCommandService;
        this.menuBuilderFactory = menuBuilderFactory;
        this.buttonBuilderFactory = buttonBuilderFactory;
        this.specializationMenuFactory = specializationMenuFactory;
        this.dailyLoginMenuFactory = dailyLoginMenuFactory;
        this.teamMenuFactory = teamMenuFactory;
        this.collectablesMenuFactory = collectablesMenuFactory;
        this.challengesMenuFactory = challengesMenuFactory;
        this.shopMenuFactory = shopMenuFactory;
        this.helpMenuFactory = helpMenuFactory;
        this.stringUtils = stringUtils;
    }

    List<String> createCombatView(LegendsPlayer player) {
        List<String> combatView = new ArrayList<>();
        String specialization = player.getSpecialization().getName();
        String specializationPrefix = stringUtils.isVowel(specialization.charAt(0)) ? "an " : "a ";
        String team = player.getTeam() == null
                ? "Wayfarers"
                : player.getTeam().getPlural();
        combatView.add("You are currently " + specializationPrefix + YELLOW + specialization);
        combatView.add("fighting for the " + YELLOW + team);
        return combatView;
    }

    List<String> createRankView(LegendsPlayer player) {
        Rank rank = player.getRank();
        String formattedRank = rank.getFormattedName();

        List<String> rankView = new ArrayList<>();
        rankView.add("Rank:");
        rankView.add(rank.getColor() + formattedRank);
        return rankView;
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
        createCombatView(player).forEach(builder::addTooltip);
        builder.addTooltip("");
        createRankView(player).forEach(builder::addTooltip);
        builder.addTooltip("");
        createAttributesView(player).forEach(builder::addTooltip);
        builder.addTooltip("");
        createCurrenciesView(player).forEach(builder::addTooltip);
        builder.addTooltip("");
        builder.addTooltip("[LEFT CLICK] To change your class");
        builder.addTooltip("[RIGHT CLICK] To change your race");
        if (player.getRankUpRewardsClaimed() < player.getRank().getLevel()) {
            builder.addTooltip("");
            builder.addTooltip(ColorCode.GREEN + "[SHIFT RIGHT CLICK] To claim your rank up rewards");
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

    Button createDailyLoginButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.CAKE, "Daily Login");
        builder.addTooltip("Every day you can get a reward just for");
        builder.addTooltip("logging in! The more often you log in, the");
        builder.addTooltip("more interesting the rewards. And don't");
        builder.addTooltip("worry; missing a day or two does not reset");
        builder.addTooltip("your progress.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view your rewards");

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

    Button createTeleportButton(LegendsPlayer player) {
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.FLINT, "Monolith Shard");
        builder.addTooltip("A pitch black splinter that is said to have");
        builder.addTooltip("once been part of The Monolith. It can take");
        builder.addTooltip("you back to where The Monolith's power was");
        builder.addTooltip("once at its strongest.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To view teleport locations");

        builder.withAction(action -> {

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
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
        MenuBuilder builder = menuBuilderFactory.createMenuBuilder("WarCraft: Legends", MenuSize.ONE_ROW);
        builder.addButton(0, createPlayerButton(player));
        builder.addButton(1, createDailyLoginButton(player));
        builder.addButton(2, createChallengesButton(player));
        builder.addButton(3, createCollectablesButton(player));
        builder.addButton(4, createShopButton(player));
        // empty slot 5
        // empty slot 6
        builder.addButton(7, createTeleportButton(player));
        builder.addButton(8, createHelpButton(player));
        return builder.build();
    }
}
