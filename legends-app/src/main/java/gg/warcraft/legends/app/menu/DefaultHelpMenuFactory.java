package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.HelpMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
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

import java.util.UUID;

public class DefaultHelpMenuFactory extends AbstractMenuFactory implements HelpMenuFactory {
    private final MainMenuFactory mainMenuFactory;

    @Inject
    public DefaultHelpMenuFactory(MenuCommandService menuCommandService, MenuBuilderFactory menuBuilderFactory,
                                  ButtonBuilderFactory buttonBuilderFactory, MainMenuFactory mainMenuFactory) {
        super(menuCommandService, menuBuilderFactory, buttonBuilderFactory);
        this.mainMenuFactory = mainMenuFactory;
    }

    Button createInfoButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Help Menu");
        builder.addTooltip("From housing decorations to mini pets");
        builder.addTooltip("and more. There's a lot to collect on");
        builder.addTooltip("the server. Gotta collect 'em all!");
        return builder.build();
    }

    Button createServerButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "The Server");
        builder.addTooltip("Welcome to WarCraft: Legends wayfarer! You");
        builder.addTooltip("are currently standing on Ouros, once the");
        builder.addTooltip("moon of Alerak it was shattered by the Titans");
        builder.addTooltip("for reasons unknown.");
        builder.addTooltip("");
        builder.addTooltip("The greatest warriors known to time battle");
        builder.addTooltip("it out in this eternal conflict and it appears");
        builder.addTooltip("you too have been summoned.");
        return builder.build();
    }

    Button createRacesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Races");
        builder.addTooltip("Ouros is home to 2 great races: Humans and Orcs.");
        builder.addTooltip(""); // TODO talk about color coding
        builder.addTooltip("You will find yourself on a team-based battlefield,");
        builder.addTooltip("meaning that whoever is not on your team is your");
        builder.addTooltip("enemy. And while it is possible to change your");
        builder.addTooltip("race there are some time or cost restrictions,");
        builder.addTooltip("so choose wisely.");
        builder.addTooltip("");
        builder.addTooltip("You can open the race menu via the main menu");
        builder.addTooltip("(/help) and then [SHIFT RIGHT CLICK] on yourself.");
        return builder.build();
    }

    Button createClassesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Classes");
        builder.addTooltip("The Titans have bestowed upon every wayfarer");
        builder.addTooltip("the power to channel latent magic found around");
        builder.addTooltip("the moon for combat purposes.");
        builder.addTooltip("");
        builder.addTooltip("Combat classes are split up into three branches:");
        builder.addTooltip("Warrior, Ranger, and Mage. Every class has four");
        builder.addTooltip("abilities bound to a certain shift key and mouse");
        builder.addTooltip("click combination.");
        builder.addTooltip("");
        builder.addTooltip("You can open the class menu via the main menu");
        builder.addTooltip("(/help) and then [SHIFT LEFT CLICK] on yourself.");
        return builder.build();
    }

    Button createObjectivesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Objectives");
        builder.addTooltip("Scattered across the moon are capturable");
        builder.addTooltip("objectives that'll allow your race to fortify");
        builder.addTooltip("its position in hostile territory and press");
        builder.addTooltip("the advantage on enemy resources.");
        builder.addTooltip("");
        builder.addTooltip("Every objective has a capture circle in its");
        builder.addTooltip("courtyard. You capture an objective by standing");
        builder.addTooltip("in this circle with your team, but depending");
        builder.addTooltip("on the size of the objective it could take a while");
        builder.addTooltip("to capture it."); // TODO reword this section
        builder.addTooltip("");
        builder.addTooltip("Every island has 2 Keeps, 2 Outposts, and");
        builder.addTooltip("3 Resource camps.");
        return builder.build();
    }

    Button createResourcesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Resources");
        builder.addTooltip("");
        builder.addTooltip("");
        builder.addTooltip("Starve your opponents by cutting of their food");
        builder.addTooltip("supply or severely penalize their ability to");
        builder.addTooltip("build siege weapons by occupying their lumber");
        builder.addTooltip("yard or mine.");
        return builder.build();
    }

    Button createSiegeWeaponsButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Siege Weapons");
        builder.addTooltip("Some objectives are better defended than others");
        builder.addTooltip("and might need some more 'convincing' before it");
        builder.addTooltip("is able to be captured.");
        builder.addTooltip("");
        builder.addTooltip("");
        return builder.build();
    }

    Button createWarsButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Wars");
        builder.addTooltip("The months on Ouros are identified by Wars.");
        builder.addTooltip("Every month a new war ");
        return builder.build();
    }

    Button createSkirmishesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Skirmishes");

        return builder.build();
    }

    Button createRewardsButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Rewards");

        return builder.build();
    }

    Button createChallengesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Rewards");

        return builder.build();
    }

    Button createLeaderboardsButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Rewards");

        return builder.build();
    }

    Button createShopButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Rewards");

        return builder.build();
    }

    @Override
    public Menu createHelpMenu(UUID playerId) {
        MenuBuilder builder = createMenuBuilder("Help Menu", MenuSize.TWO_ROWS);
        builder.addButton(0, createInfoButton());
        builder.addButton(9, createBackButton(playerId, mainMenuFactory::createMainMenu));

        builder.addButton(2, createServerButton());
        builder.addButton(3, createRacesButton());
        builder.addButton(4, createClassesButton());
        builder.addButton(5, createObjectivesButton());
        builder.addButton(6, createResourcesButton());
        builder.addButton(7, createSiegeWeaponsButton());

        builder.addButton(9, createWarsButton());
        builder.addButton(10, createSkirmishesButton());
        builder.addButton(11, createRewardsButton());
        builder.addButton(12, createChallengesButton());
        builder.addButton(13, createLeaderboardsButton());
        builder.addButton(14, createShopButton());

        return builder.build();
    }
}
