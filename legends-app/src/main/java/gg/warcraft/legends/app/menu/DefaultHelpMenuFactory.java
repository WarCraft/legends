package gg.warcraft.legends.app.menu;

import com.google.inject.Inject;
import gg.warcraft.legends.api.menu.HelpMenuFactory;
import gg.warcraft.legends.api.menu.MainMenuFactory;
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
import gg.warcraft.monolith.api.world.item.ItemType;

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
        builder.addTooltip("This is the Help Menu. In here we've compiled");
        builder.addTooltip("a list of all the things you need to know to");
        builder.addTooltip("start playing on the server.");
        builder.addTooltip("");
        builder.addTooltip("Think of it as the Server Manual. If after");
        builder.addTooltip("reading this menu you still have more questions");
        builder.addTooltip("do not hesitate to contact a member of staff!");
        return builder.build();
    }

    Button createServerButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "The Server");
        builder.addTooltip("Welcome to WarCraft: Legends wayfarer! You");
        builder.addTooltip("are currently standing on Ouros. Once the");
        builder.addTooltip("moon of Alerak, it has since been shattered");
        builder.addTooltip("by the Titans for reasons unknown.");
        builder.addTooltip("");
        builder.addTooltip("The greatest warriors known to time battle");
        builder.addTooltip("it out in its eternal conflict and it appears");
        builder.addTooltip("you too have been summoned.");
        return builder.build();
    }

    Button createRacesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Races");
        builder.addTooltip("Ouros is home to 2 great races: Humans and Orcs.");
        builder.addTooltip("Once you have given your allegiance all other races");
        builder.addTooltip("will become your sworn enemies, so choose wisely!");
        builder.addTooltip("");
        builder.addTooltip("To make it easier to distinguish between what belongs");
        builder.addTooltip("to which race all races are color coded. You can");
        builder.addTooltip("find the color legend at the top of the TAB-list.");
        builder.addTooltip("For example: as a Human your race's color is blue,");
        builder.addTooltip("and you will only be able to enter blue portals.");
        builder.addTooltip("");
        builder.addTooltip("(It is possible to change your race, but there are");
        builder.addTooltip("some time or cost restrictions).");
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
        builder.addTooltip("click combination. You unlock one class in the");
        builder.addTooltip("tutorial and the others with the Favor you earn!");
        builder.addTooltip("");
        builder.addTooltip("You can open the class menu via the main menu");
        builder.addTooltip("(/help) and then [SHIFT LEFT CLICK] on yourself.");
        return builder.build();
    }

    Button createObjectivesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Objectives");
        builder.addTooltip("Scattered across the map are capturable");
        builder.addTooltip("objectives that'll allow your race to fortify");
        builder.addTooltip("its position in friendly or hostile territory and");
        builder.addTooltip("press the advantage on enemy resources.");
        builder.addTooltip("");
        builder.addTooltip("Every objective has a capture circle in its");
        builder.addTooltip("courtyard. You capture an objective by standing");
        builder.addTooltip("in this circle with your team, but depending");
        builder.addTooltip("on the size of the objective it could take a while");
        builder.addTooltip("to capture it.");
        builder.addTooltip("");
        builder.addTooltip("Every island has 2 Keeps, 2 Outposts, and");
        builder.addTooltip("3 Resource camps.");
        return builder.build();
    }

    Button createResourcesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Resources");
        builder.addTooltip("One special objective type is the resource camp.");
        builder.addTooltip("Resource camps allow the current occupier to gather");
        builder.addTooltip("food, wood, or stone. Food is necessary to keep your");
        builder.addTooltip("army from dying and wood and stone are used to build");
        builder.addTooltip("siege weaponry!");
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
        builder.addTooltip("With a siege blueprint purchased at your spawn and");
        builder.addTooltip("enough wood and stone you can place down trebuchets");
        builder.addTooltip("do destroy enemy fortifications from a distance.");
        builder.addTooltip("Once placed the treb needs to be loaded with stone,");
        builder.addTooltip("you can then use the buttons to tilt and charge the");
        builder.addTooltip("treb after which a final pull of the leaver fires");
        builder.addTooltip("the projectile!");
        return builder.build();
    }

    Button createHousingButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Housing");
        builder.addTooltip("You can rent your own house in your capital city!");
        builder.addTooltip("Not only that, but you can completely decorate it");
        builder.addTooltip("however your see fit, from the floors and the stairs");
        builder.addTooltip("to that very particular ornament in the corner.");
        builder.addTooltip("");
        builder.addTooltip("You can rent a house by right clicking on its sign");
        builder.addTooltip("outside. If the house is not currently owned you'll");
        builder.addTooltip("have to pay the upfront price once, and then a");
        builder.addTooltip("recurring price every 2 weeks.");
        builder.addTooltip("");
        builder.addTooltip("You can leave your house early via the same sign,");
        builder.addTooltip("but beware that any items still in the house are lost!");
        return builder.build();
    }

    Button createWarsButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Wars");
        builder.addTooltip("The months on Ouros are identified by its Wars.");
        builder.addTooltip("A war runs from the start till the end of a month.");
        builder.addTooltip("Throughout a war point tallies occur. Every point");
        builder.addTooltip("tally all the objectives that your race currently");
        builder.addTooltip("owns are put together and assigned a score. This");
        builder.addTooltip("score is then added to your daily points. The race");
        builder.addTooltip("with the highest amount of points at the end of the");
        builder.addTooltip("day will be awarded with 5 War Score. The final");
        builder.addTooltip("War Score determines which race wins the war at the");
        builder.addTooltip("end of the month.");
        return builder.build();
    }

    Button createSkirmishesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Skirmishes");
        builder.addTooltip("To keep everyone on their toes and to provide the");
        builder.addTooltip("underdogs with a chance of stealing a few War Score");
        builder.addTooltip("every day there will be 3 Skirmishes.");
        builder.addTooltip("");
        builder.addTooltip("At the start of a Skirmish all players are teleported");
        builder.addTooltip("back to their capital after which a special 30 minute");
        builder.addTooltip("event starts where player kills award 5x as many points!");
        builder.addTooltip("The race with the most points at the end of the Skirmish");
        builder.addTooltip("will be awarded 1 War Score.");
        builder.addTooltip("");
        builder.addTooltip("These Skirmishes happen at 3PM, 6PM, and 9PM New York");
        builder.addTooltip("time and you can find a countdown timer in the TAB-list.");
        builder.addTooltip("Make sure you bring your friends!");
        return builder.build();
    }

    Button createRewardsButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Rewards");
        builder.addTooltip("Playing on the server gives you a steady income of Favor.");
        builder.addTooltip("Favor is used both to increase your level as well as");
        builder.addTooltip("the default currency for account upgrades such as");
        builder.addTooltip("unlocking additional combat classes.");
        builder.addTooltip("");
        builder.addTooltip("Most activities on the server, such as capturing an");
        builder.addTooltip("objective and most notably winning a war, awards you");
        builder.addTooltip("with crates. Crates come in various shapes and sizes");
        builder.addTooltip("and also contain loot in various rarities and prizes.");
        builder.addTooltip("You can even loot shop items from higher rarity crates!");
        builder.addTooltip("");
        builder.addTooltip("Mobs drop gold which is used to pay for your house and");
        builder.addTooltip("trading with other players.");
        return builder.build();
    }

    Button createChallengesButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Challenges");
        builder.addTooltip("Challenges are a simple way of adding a");
        builder.addTooltip("little bit of extra action to your day to");
        builder.addTooltip("day activities on the server.");
        builder.addTooltip("");
        builder.addTooltip("There are 3 types of challenges on the server:");
        builder.addTooltip("Daily, War, and All-time challenges.");
        builder.addTooltip("The daily challenges are different every");
        builder.addTooltip("day. Small tasks for small rewards.");
        builder.addTooltip("The war challenges are tied to a war and");
        builder.addTooltip("as such reset every month. They take a little");
        builder.addTooltip("more effort to complete, but their rewards");
        builder.addTooltip("are substantially better.");
        builder.addTooltip("");
        builder.addTooltip("can you complete them all?");
        return builder.build();
    }

    Button createLeaderboardsButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "Leaderboards");
        builder.addTooltip("Why make a legend if no one will document");
        builder.addTooltip("your tales?");
        builder.addTooltip("");
        builder.addTooltip("All of your achievements on WarCraft: Legends");
        builder.addTooltip("are documented and pitted against every other");
        builder.addTooltip("wayfarer on the WarCraft Leaderboards.");
        builder.addTooltip("");
        builder.addTooltip("Check out who is currently owning the hardest at");
        builder.addTooltip(ColorCode.YELLOW + "warcraft.gg/leaderboard");
        return builder.build();
    }

    Button createShopButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "WarCraft Shop");
        builder.addTooltip("Running a server is a lot of fun and super");
        builder.addTooltip("rewarding, but it also requires large amounts");
        builder.addTooltip("of time and funds. Purchasing items in the");
        builder.addTooltip("shop is entirely optional as everything can");
        builder.addTooltip("also be obtained via loot crates that you get");
        builder.addTooltip("for simply playing on the server.");
        builder.addTooltip("");
        builder.addTooltip("To go even further beyond in supporting the");
        builder.addTooltip("server the WarCraft Shop has six different");
        builder.addTooltip("items for sale every day. These items can");
        builder.addTooltip("be bought with Fragments which are available");
        builder.addTooltip("from the web shop at " + ColorCode.YELLOW + "shop.warcraft.gg");
        return builder.build();
    }

    Button createStaffButton() {
        ButtonBuilder builder = createSimpleButtonBuilder(ItemType.BOOK, "WarCraft Staff");
        builder.addTooltip("You made it! Your eached the end of the");
        builder.addTooltip("server manual. If at any point during your");
        builder.addTooltip("stay you have any more questions or have run");
        builder.addTooltip("into an issue please do not hesitate to get");
        builder.addTooltip("in touch with anyone of the staff team.");
        builder.addTooltip("");
        builder.addTooltip("We're always happy to help!");
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
        builder.addButton(8, createHousingButton());

        builder.addButton(11, createWarsButton());
        builder.addButton(12, createSkirmishesButton());
        builder.addButton(13, createRewardsButton());
        builder.addButton(14, createChallengesButton());
        builder.addButton(15, createLeaderboardsButton());
        builder.addButton(16, createShopButton());
        builder.addButton(17, createStaffButton());

        return builder.build();
    }
}
