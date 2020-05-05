package gg.warcraft.legends.api.menu

import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.item.ItemType
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService}
import gg.warcraft.monolith.api.player.Player

object HelpMenu {
  def apply(
      player: Player
  )(implicit
      menuService: MenuService
  ): Menu = {
    val infoButton = Button(
      ItemType.BOOK,
      "Help Menu",
      List(
        "This is the Help Menu. In here we've compiled",
        "a list of all the things you need to know to",
        "start playing on the server.",
        "",
        "Think of it as the Server Manual. If after",
        "reading this menu you still have more questions",
        "do not hesitate to contact a member of staff!"
      )
    )

    val serverButton = Button(
      ItemType.BOOK,
      "WarCraft: Legends",
      List(
        "Welcome to WarCraft: Legends wayfarer! You",
        "are currently standing on Ouros. Once the",
        "moon of Alerak, it has since been shattered",
        "by the Titans for reasons unknown.",
        "",
        "The greatest warriors known to time battle",
        "it out in its eternal conflict and it appears",
        "you too have been summoned."
      )
    )

    val racesButton = Button(
      ItemType.BOOK,
      "4 Races",
      List(
        "Ouros is home to 2 great races: Humans and Orcs.",
        "Once you have given your allegiance all other races",
        "will become your sworn enemies, so choose wisely!",
        "",
        "To make it easier to distinguish between what belongs",
        "to which race all races are color coded. You can",
        "find the color legend at the top of the TAB-list.",
        "For example: as a Human your race's color is blue,",
        "and you will only be able to enter blue portals.",
        "",
        "(It is possible to change your race, but there are",
        "some time or cost restrictions).",
        "You can open the race menu via the main menu",
        "(/help) and then [SHIFT RIGHT CLICK] on yourself."
      )
    )

    val combatClassesButton = Button(
      ItemType.BOOK,
      "Combat Classes",
      List(
        "The Titans have bestowed upon every wayfarer",
        "the power to channel latent magic found around",
        "the moon for combat purposes.",
        "",
        "Combat classes are split up into three branches:",
        "Warrior, Ranger, and Mage. Every class has four",
        "abilities bound to a certain shift key and mouse",
        "click combination. You unlock one class in the",
        "tutorial and the others with the Favor you earn!",
        "",
        "You can open the class menu via the main menu",
        "(/help) and then [SHIFT LEFT CLICK] on yourself."
      )
    )

    val objectivesButton = Button(
      ItemType.BOOK,
      "Objectives",
      List(
        "Scattered across the map are capturable",
        "objectives that'll allow your race to fortify",
        "its position in friendly or hostile territory and",
        "press the advantage on enemy resources.",
        "",
        "Every objective has a capture circle in its",
        "courtyard. You capture an objective by standing",
        "in this circle with your team, but depending",
        "on the size of the objective it could take a while",
        "to capture it.",
        "",
        "Every island has 2 Keeps, 2 Outposts, and",
        "3 Resource camps."
      )
    )

    val resourcesButton = Button(
      ItemType.BOOK,
      "Resources",
      List(
        "One special objective type is the resource camp.",
        "Resource camps allow the current occupier to gather",
        "food, wood, or stone. Food is necessary to keep your",
        "army from dying and wood and stone are used to build",
        "siege weaponry!",
        "",
        "Starve your opponents by cutting of their food",
        "supply or severely penalize their ability to",
        "build siege weapons by occupying their lumber",
        "yard or mine."
      )
    )

    val siegeButton = Button(
      ItemType.BOOK,
      "Siege Weapons",
      List(
        "Some objectives are better defended than others",
        "and might need some more 'convincing' before it",
        "is able to be captured.",
        "",
        "With a siege blueprint purchased at your spawn and",
        "enough wood and stone you can place down trebuchets",
        "do destroy enemy fortifications from a distance.",
        "Once placed the treb needs to be loaded with stone,",
        "you can then use the buttons to tilt and charge the",
        "treb after which a final pull of the leaver fires",
        "the projectile!"
      )
    )

    val houseButton = Button(
      ItemType.BOOK,
      "Housing",
      List(
        "You can rent your own house in your capital city!",
        "Not only that, but you can completely decorate it",
        "however your see fit, from the floors and the stairs",
        "to that very particular ornament in the corner.",
        "",
        "You can rent a house by right clicking on its sign",
        "outside. If the house is not currently owned you'll",
        "have to pay the upfront price once, and then a",
        "recurring price every 2 weeks.",
        "",
        "You can leave your house early via the same sign,",
        "but beware that any items still in the house are lost!"
      )
    )

    val warButton = Button(
      ItemType.BOOK,
      "Warfare",
      List(
        "The months on Ouros are identified by its Wars.",
        "A war runs from the start till the end of a month.",
        "Throughout a war point tallies occur. Every point",
        "tally all the objectives that your race currently",
        "owns are put together and assigned a score. This",
        "score is then added to your daily points. The race",
        "with the highest amount of points at the end of the",
        "day will be awarded with 5 War Score. The final",
        "War Score determines which race wins the war at the",
        "end of the month."
      )
    )

    val skirmishButton = Button(
      ItemType.BOOK,
      "Skirmishes",
      List(
        "To keep everyone on their toes and to provide the",
        "underdogs with a chance of stealing a few War Score",
        "every day there will be 3 Skirmishes.",
        "",
        "At the start of a Skirmish all players are teleported",
        "back to their capital after which a special 30 minute",
        "event starts where player kills award 5x as many points!",
        "The race with the most points at the end of the Skirmish",
        "will be awarded 1 War Score.",
        "",
        "These Skirmishes happen at 3PM, 6PM, and 9PM New York",
        "time and you can find a countdown timer in the TAB-list.",
        "Make sure you bring your friends!"
      )
    )

    val rewardsButton = Button(
      ItemType.BOOK,
      "Rewards",
      List(
        "Playing on the server gives you a steady income of Favor.",
        "Favor is used both to increase your level as well as",
        "the default currency for account upgrades such as",
        "unlocking additional combat classes.",
        "",
        "Most activities on the server, such as capturing an",
        "objective and most notably winning a war, awards you",
        "with crates. Crates come in various shapes and sizes",
        "and also contain loot in various rarities and prizes.",
        "You can even loot shop items from higher rarity crates!",
        "",
        "Mobs drop gold which is used to pay for your house and",
        "trading with other players."
      )
    )

    val challengesButton = Button(
      ItemType.BOOK,
      "Challenges",
      List(
        "Challenges are a simple way of adding a",
        "little bit of extra action to your day to",
        "day activities on the server.",
        "",
        "There are 3 types of challenges on the server:",
        "Daily, War, and All-time challenges.",
        "The daily challenges are different every",
        "day. Small tasks for small rewards.",
        "The war challenges are tied to a war and",
        "as such reset every month. They take a little",
        "more effort to complete, but their rewards",
        "are substantially better.",
        "",
        "can you complete them all?"
      )
    )

    val leaderboardsButton = Button(
      ItemType.BOOK,
      "Leaderboards",
      List(
        "Why make a legend if no one will document",
        "your tales?",
        "",
        "All of your achievements on WarCraft: Legends",
        "are documented and pitted against every other",
        "wayfarer on the WarCraft Leaderboards.",
        "",
        "Check out who is currently owning the hardest at",
        ColorCode.YELLOW + "warcraft.gg/leaderboard"
      )
    )

    val shopButton = Button(
      ItemType.BOOK,
      "WarCraft: Shop",
      List(
        "Running a server is a lot of fun and super",
        "rewarding, but it also requires large amounts",
        "of time and funds. Purchasing items in the",
        "shop is entirely optional as everything can",
        "also be obtained via loot crates that you get",
        "for simply playing on the server.",
        "",
        "To go even further beyond in supporting the",
        "server the WarCraft Shop has six different",
        "items for sale every day. These items can",
        "be bought with Fragments which are available",
        "from the web shop at " + ColorCode.YELLOW + "shop.warcraft.gg"
      )
    )

    val staffButton = Button(
      ItemType.BOOK,
      "Staff",
      List(
        "You made it! Your eached the end of the",
        "server manual. If at any point during your",
        "stay you have any more questions or have run",
        "into an issue please do not hesitate to get",
        "in touch with anyone of the staff team.",
        "",
        "We're always happy to help!"
      )
    )

    Menu(
      "Help Menu",
      Menu.Size.TWO_ROWS,
      Map(
        0 -> infoButton,
        9 -> Button.back(MainMenu.apply),
        // 1st row
        2 -> serverButton,
        3 -> racesButton,
        4 -> combatClassesButton,
        5 -> objectivesButton,
        6 -> resourcesButton,
        7 -> siegeButton,
        8 -> houseButton,
        // 2nd row
        11 -> warButton,
        12 -> skirmishButton,
        13 -> rewardsButton,
        14 -> challengesButton,
        15 -> leaderboardsButton,
        16 -> shopButton,
        17 -> staffButton
      )
    )
  }
}
