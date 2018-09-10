package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.menu.DecorationsMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
import gg.warcraft.legends.api.menu.MiniPetsMenuFactory;
import gg.warcraft.legends.api.menu.SpecializationMenuFactory;
import gg.warcraft.legends.api.menu.TeamMenuFactory;
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
    private final MenuCommandService menuCommandService;
    private final MenuBuilderFactory menuBuilderFactory;
    private final ButtonBuilderFactory buttonBuilderFactory;
    private final SpecializationMenuFactory specializationMenuFactory;
    private final TeamMenuFactory teamMenuFactory;
    private final DecorationsMenuFactory decorationsMenuFactory;
    private final MiniPetsMenuFactory miniPetsMenuFactory;
    private final StringUtils stringUtils;

    @Inject
    public DefaultMainMenuFactory(LegendsPlayerQueryService legendsPlayerQueryService,
                                  MenuCommandService menuCommandService, MenuBuilderFactory menuBuilderFactory,
                                  ButtonBuilderFactory buttonBuilderFactory,
                                  SpecializationMenuFactory specializationMenuFactory,
                                  TeamMenuFactory teamMenuFactory, DecorationsMenuFactory decorationsMenuFactory,
                                  MiniPetsMenuFactory miniPetsMenuFactory, StringUtils stringUtils) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.menuCommandService = menuCommandService;
        this.menuBuilderFactory = menuBuilderFactory;
        this.buttonBuilderFactory = buttonBuilderFactory;
        this.specializationMenuFactory = specializationMenuFactory;
        this.teamMenuFactory = teamMenuFactory;
        this.decorationsMenuFactory = decorationsMenuFactory;
        this.miniPetsMenuFactory = miniPetsMenuFactory;
        this.stringUtils = stringUtils;
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
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.ARMOR_STAND, player.getName());
        createAttributesView(player).forEach(builder::addTooltip);
        builder.addTooltip("");
        createCurrenciesView(player).forEach(builder::addTooltip);
        return builder.build();
    }

    Button createSpecializationButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        String specialization = player.getSpecialization().getName();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.IRON_SWORD, "Specialization");
        String prefix = stringUtils.isVowel(specialization.charAt(0)) ? "an " : "a ";
        builder.addTooltip("You are currently fighting");
        builder.addTooltip("as " + prefix + YELLOW + specialization);
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To change your specialization");

        builder.withAction(action -> {
            Menu menu = specializationMenuFactory.createSpecializationMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createTeamButton(LegendsPlayer player) {
        UUID playerId = player.getId();
        String team = player.getTeam().getName();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.BANNER, "Team");
        builder.addTooltip("You are currently fighting");
        builder.addTooltip("for the " + YELLOW + team);
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To change your team");

        builder.withAction(action -> {
            Menu menu = teamMenuFactory.createTeamMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createDecorationsButton(UUID playerId) {
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.OAK_WOOD_PLANK, "Decorations");
        builder.addTooltip("Did you know you can rent a house?");
        builder.addTooltip("Did you know you can completely");
        builder.addTooltip("decorate it however you want!?");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your decorations");

        builder.withAction(action -> {
            Menu menu = decorationsMenuFactory.createDecorationsMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    Button createMiniPetsButton(UUID playerId) {
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.SPAWN_PIG, "Mini Pets");
        builder.addTooltip("Mini pets are cute little creatures");
        builder.addTooltip("that will follow and cheer you on");
        builder.addTooltip("wherever you go.");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To browse your mini pets");

        builder.withAction(action -> {
            Menu menu = miniPetsMenuFactory.createMiniPetsMenu(playerId);
            menuCommandService.showMenu(menu, playerId);
        });
        return builder.build();
    }

    @Override
    public Menu createMainMenu(UUID playerId) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
        MenuBuilder builder = menuBuilderFactory.createMenuBuilder("Menu", MenuSize.ONE_ROW);
        builder.addButton(0, createPlayerButton(player));
        builder.addButton(1, createSpecializationButton(player));
        builder.addButton(2, createTeamButton(player));
        builder.addButton(3, createDecorationsButton(playerId));
        builder.addButton(4, createMiniPetsButton(playerId));
        return builder.build();
    }
}
