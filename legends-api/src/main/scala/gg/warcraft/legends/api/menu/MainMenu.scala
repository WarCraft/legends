/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.{CombatClass, CombatClassService}
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.combatplayer._
import gg.warcraft.legends.api.combat.LegendsCombatClass._
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.{Level, LevelService}
import gg.warcraft.legends.api.util.legendsplayer._
import gg.warcraft.monolith.api.block.variant.FlowerVariant
import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.item.{ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.util.string._

object MainMenu {
  private final val trackedAttributes =
    Strength :: Dexterity :: Intellect :: Vitality :: Crit :: Nil

  private def attributesView(player: Player): List[String] = {
    val attributes = player.attributes
    val formattedAttributes = trackedAttributes.map { attribute =>
      val value = attributes.get(attribute).toInt
      if (value != 0) Some(s"$YELLOW$value ${attribute.name}") else None
    }.filter { _.isDefined }.map { _.get }
    s"${ColorCode.WHITE}Attributes:" :: formattedAttributes
  }

  private final val trackedCurrencies =
    Favor :: Fragments :: Gold :: Nil

  private def currenciesView(player: Player): List[String] = {
    val currencies = player.currencies
    val formattedCurrencies = trackedCurrencies.map { currency =>
      s"$YELLOW${currencies.get(currency)} ${currency.name}"
    }
    s"${ColorCode.WHITE}Currencies:" :: formattedCurrencies
  }

  private def playerButton(player: Player): Button = Button.skull(
    player.name,
    player.name,
    ("This is you!" :: "" :: Nil)
      ++ attributesView(player)
      ++ ("" :: Nil)
      ++ currenciesView(player)
    /*
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
          }
      });
      return builder.build();
     */
  ) { _ => () }

  private def levelPassivesView(level: Level): List[String] = {
    "None" :: Nil // TODO
  }

  /*
  List<String> createRankPassivesView(int level) {
        int prestige = level / 50; // NOTE this is hacky, 49/50 is the prestige level
        int clippedLevel = level % 50; // NOTE this is hacky, 49/50 is the prestige level
        int attributeValue = PrestigeAttributesHandler.attributeModifierValues.get(clippedLevel / 7);

        List<String> passivesView = new ArrayList<>();
        if (prestige == 0) {
            if (attributeValue > 0) {
                passivesView.add(ColorCode.YELLOW + "+" + attributeValue + " Vitality");
            } else {
                passivesView.add("None");
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
   */

  private def levelIcon(level: Level): ItemTypeOrVariant = level.value % 10 match {
    case 0 => ItemType.PLANKS
    case 1 => ItemType.STONE
    case 2 => ItemType.IRON_BLOCK
    case 3 => ItemType.REDSTONE_BLOCK
    case 4 => ItemType.LAPIS_BLOCK
    case 5 => ItemType.GOLD_BLOCK
    case 6 => ItemType.EMERALD_BLOCK
    case 7 => ItemType.DIAMOND_BLOCK
    case _ => ItemType.NETHER_STAR
  }

  private def levelButton(player: Player)(implicit
      levelService: LevelService
  ): Button = {
    val level = player.level
    Button(
      levelIcon(level),
      "Level",
      (s"${WHITE}Current level:"
        :: s"${YELLOW}Lv. ${level.value}" // TODO show percent to next (${%}%)
        :: ""
        :: s"${WHITE}Unlocked passives:" :: Nil)
        ++ levelPassivesView(level)
    ) { _ => () }
  }

  private def abilitiesView(combatClass: Option[CombatClass]): List[String] =
    combatClass match {
      case Some(it) => (
          s"[Left Click] ${it.leftClick.getOrElse("Basic Attack")}"
            :: s"[Right Click] ${it.rightClick.getOrElse("Basic Attack")}"
            :: s"[Shift Left Click] ${it.shiftLeftClick.getOrElse("Basic Attack")}"
            :: s"[Shift Right Click] ${it.shiftRightClick.getOrElse("Basic Attack")}"
            :: Nil
        )

      case None => "None" :: Nil
    }

  private def classButton(
      combatClass: Option[CombatClass]
  )(implicit
      menuService: MenuService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService
  ): Button = {
    val name = combatClass.map { _.name }.getOrElse("Wayfarer")
    val prefix = if (name(0).isVowel) "an" else "a"
    Button(
      combatClass.map { _.mainHands.head }.getOrElse(FlowerVariant.DANDELION),
      "Class",
      (s"You are fighting as $prefix $YELLOW$name"
        :: ""
        :: s"${WHITE}Abilities:" :: Nil)
        ++ abilitiesView(combatClass)
        ++ (""
          :: "[Click] to change your class" :: Nil)
    ) { click =>
      menuService.showMenu(click.player.id, CombatClassMenu(click.player))
    }
  }

  private def racialPassiveView(
      player: Player,
      team: Team,
      combatClassOption: Option[CombatClass]
  ): List[String] = {
    val combatClass = combatClassOption.map { _.name }.getOrElse("Unarmed")
    (team, combatClass) match {
      // Warrior
      case (Dwarves, Berserker.name | Guardian.name) => (
          s"${YELLOW}Sturdiness"
            :: "You take up to 25% less damage depending"
            :: "on how much damage you've already taken."
            :: Nil
        )
      case (Elves, Berserker.name | Guardian.name) => (
          s"${YELLOW}Blade Dance"
            :: "Every 3rd outgoing melee attack you'll dodge"
            :: "the first incoming melee attack for 3 seconds."
            :: Nil
        )
      case (Humans, Berserker.name | Guardian.name) => (
          s"${YELLOW}Last Stand"
            :: "Upon receiving a fatal blow you instead"
            :: "become invulnerable for 3 seconds."
            :: Nil
        )
      case (Orcs, Berserker.name | Guardian.name) => (
          s"${YELLOW}Bloodlust"
            :: "While below half health your crit chance"
            :: "is doubled."
            :: Nil
        )

      // Hunter
      case (_, Marksman.name | Ranger.name) => (
          s"${YELLOW}Hunter Pet"
            :: "Your pet fights by your side, attacking"
            :: "everything you're attacking."
            :: Nil
        )

      // Mage
      case (Dwarves, Elementalist.name | Wizard.name) => (
          s"${YELLOW}Unstable Bunker"
            :: "When falling below half health and"
            :: "after a short delay you damage and push"
            :: "nearby enemies back."
            :: Nil
        )
      case (Elves, Elementalist.name | Wizard.name) => (
          s"${YELLOW}Regrowth"
            :: Nil
        )
      case (Humans, Elementalist.name | Wizard.name) => (
          s"${YELLOW}Arcane Barrier"
            :: "When falling below half health you"
            :: "receive an arcane shield."
            :: Nil
        )
      case (Orcs, Elementalist.name | Wizard.name) => (
          s"${YELLOW}Flame Totem"
            :: "When falling below 75% health you"
            :: "summon a totem to attack nearby enemies."
            :: Nil
        )

      // Default
      case _ => s"${GRAY}None" :: Nil
    }
  }

  private def raceButton(
      player: Player,
      team: Team,
      combatClass: Option[CombatClass]
  )(implicit
      menuService: MenuService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      combatClassService: CombatClassService,
      legendsPlayerDataService: LegendsPlayerDataService
  ): Button = Button(
    ItemType.ARMOR_STAND,
    "Race",
    (s"You are fighting for the $YELLOW${team.name}"
      :: ""
      :: s"${WHITE}Racial passive:"
      :: s"" :: Nil)
      ++ racialPassiveView(player, team, combatClass)
      ++ (""
        :: "[Click] to change your race" :: Nil)
  ) { click => menuService.showMenu(click.player.id, RaceMenu(click.player)) }

  private def itemStorageButton(player: Player): Button = {
    ???
    /*
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.CHEST, "Item Storage");
        builder.addTooltip("It is possible to recieve rewards while offline,");
        builder.addTooltip("such as winning a daily leaderboard. These items");
        builder.addTooltip("can be claimed from this menu at any time.");
        builder.addTooltip("");

        builder.addTooltip("CURRENTLY UNAVAILABLE");

        // TODO
        // List<Item> storedItems = itemStorageService.getItems(playerId);
//        if (storedItems.isEmpty()) {
//            builder.addTooltip("There are no items currently waiting for you");
//        } else {
//            builder.addTooltip("You have " + storedItems.size() + " items waiting for you.");
//            builder.addTooltip("[CLICK] To receive your items");
//
//            ItemClaimTask itemClaimTask = new ItemClaimTask(itemStorageService,
//                    itemStorageService, playerQueryService, playerCommandService,
//                    taskService, playerId);
//            taskService.runNextTick(itemClaimTask);
//
//            menuCommandService.closeMenu(playerId);
//        }

        return builder.build();
     */
  }

  private def returnButton(player: Player): Button = {
    ???
    /*
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
     */
  }

  private def helpButton(player: Player)(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService
  ): Button = Button(
    ItemType.BOOK,
    "Help Menu",
    "Read the Server Manual or contact a member"
      :: "of staff if you have any more questions."
      :: ""
      :: "[Click] To open the Help Menu"
      :: Nil
  ) { click => menuService.showMenu(click.player.id, HelpMenu(click.player)) }

  private def dailyLoginButton(player: Player): Button = {
    ???
    /*
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.CLOCK, "Daily Login");
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
     */
  }

  private def dailyVoteButton(player: Player): Button = {
    ???
    /*
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.PAPER, "Daily Votes");
        builder.addTooltip("Votes text placeholder");
        builder.addTooltip("");
        builder.addTooltip(ColorCode.WHITE + "Websites:");
        builder.addTooltip(ColorCode.YELLOW + "minecraft-server-list.com");
        builder.addTooltip(ColorCode.YELLOW + "minecraftservers.org");
        builder.addTooltip(ColorCode.YELLOW + "minecraft-server.net");
        builder.addTooltip(ColorCode.YELLOW + "minecraft-mp.com");
        builder.addTooltip(ColorCode.YELLOW + "minecraftservers.biz");
        builder.addTooltip("");
        builder.addTooltip("[CLICK] To paste the links into your chat");

        builder.withAction(action -> {
            playerCommandService.sendNotification(playerId, "https://minecraft-server-list.com/server/423343/vote/");
            playerCommandService.sendNotification(playerId, "https://minecraftservers.org/vote/257786/");
            playerCommandService.sendNotification(playerId, "https://minecraft-server.net/vote/warcraftgg/");
            playerCommandService.sendNotification(playerId, "https://minecraft-mp.com/server/192075/vote/");
            playerCommandService.sendNotification(playerId, "https://minecraftservers.biz/servers/106701/");
            menuCommandService.closeMenu(playerId);
        });
        return builder.build();
     */
  }

  private def challengesButton(player: Player): Button = {
    ???
    /*
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
     */
  }

  private def collectablesButton(player: Player): Button = {
    ???
    /*
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
     */
  }

  private def leaderboardsButton(player: Player): Button = {
    ???
    /*
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
     */
  }

  private def shopButton(player: Player): Button = {
    ???
    /*
        UUID playerId = player.getId();
        ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(ItemType.GOLDEN_APPLE, "WarCraft: Shop"); // TODO enchanted golden apple
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
     */
  }

  /*
   Button createRankButton(LegendsPlayer player) {
      UUID playerId = player.getId();
      String rank = player.getRank().getColor() + player.getRank().getFormattedName();
      ItemType rankIcon = getRankIcon(rank);
      int level = player.getRank().getLevel();
      int levelUpPercentage = (int) (((float) (player.getLifetimeFavor() - player.getRank().getFavor()) /
              (float) player.getRank().getRequiredFavor() - player.getRank().getFavor()) * 100);
      ButtonBuilder builder = buttonBuilderFactory.createSimpleButtonBuilder(rankIcon, "Level & Rank");
      builder.addTooltip("Rank text");
      builder.addTooltip("");
      builder.addTooltip(ColorCode.WHITE + "Current level:");
      builder.addTooltip(ColorCode.YELLOW + "Lv. " + level + " (" + levelUpPercentage + "%)");
      builder.addTooltip("");
      builder.addTooltip(ColorCode.WHITE + "Current Rank:");
      builder.addTooltip(rank);
      builder.addTooltip("");
      builder.addTooltip(ColorCode.WHITE + "Rank passives:");
      createRankPassivesView(level).forEach(builder::addTooltip);
      return builder.build();
  }
   */

  def apply(player: Player)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      attributesService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService
  ): Menu = {
    val team = player.team.getOrElse(Wayfarers)
    val combatClass = player.combatClass
    Menu(
      "WarCraft: Legends",
      MenuSize.ONE_ROW,
      Map(
        // 1st row
        0 -> playerButton(player),
        1 -> levelButton(player),
        2 -> classButton(combatClass),
        3 -> raceButton(player, team, combatClass),
        // 4
        // TODO 5 -> itemStorageButton(player),
        // TODO 6 -> returnButton(player),
        // 7
        8 -> helpButton(player)
        // 2nd row
        // TODO 9 -> dailyLoginButton(player),
        // TODO 10 -> dailyVoteButton(player),
        // TODO 11 -> challengesButton(player),
        // 12
        // 13
        // TODO 14 -> collectablesButton(player),
        // TODO 15 -> leaderboardsButton(player),
        // 16
        // TODO 17 -> shopButton(player)
      )
    )
  }
}
