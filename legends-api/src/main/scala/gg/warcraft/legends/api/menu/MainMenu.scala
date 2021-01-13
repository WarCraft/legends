/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.{CombatClass, CombatClassService}
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.ai.MiniPetFactory
import gg.warcraft.legends.api.combat.LegendsCombatClass.{
  LegendsCombatClassOps => _, _
}
import gg.warcraft.legends.api.core.leaderboard.LeaderboardService
import gg.warcraft.legends.api.core.spawn.SpawnService
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.entity.LegendsTeam._
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.player.calendar.LoginCalendarService
import gg.warcraft.legends.api.player.challenge.ChallengeService
import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.{Level, LevelService}
import gg.warcraft.legends.api.shop.ShopService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.block.variant.FlowerVariant
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.core.FormatCode
import gg.warcraft.monolith.api.core.command.CommandService
import gg.warcraft.monolith.api.core.data.ServerDataService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.team.{Team, TeamService}
import gg.warcraft.monolith.api.item.{ItemService, ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.menu.Button.{closeMenu, noop, updateMenu}
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
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
    s"${WHITE}Attributes:" :: formattedAttributes
  }

  private final val trackedCurrencies =
    Favor :: Fragments :: Gold :: Nil

  private def currenciesView(player: Player): List[String] = {
    val currencies = player.currencies
    val formattedCurrencies = trackedCurrencies.map { currency =>
      s"$YELLOW${currencies.get(currency)} $currency"
    }
    s"${WHITE}Currencies:" :: formattedCurrencies
  }

  private def playerButton(player: Player): Button = Button.skull(
    player.name,
    player.name,
    List("This is you!", "")
      ++ attributesView(player)
      ++ List("")
      ++ currenciesView(player): _*
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
  ) { _ => noop }

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
      List(
        s"${WHITE}Current level:",
        s"${YELLOW}Lv. ${level.value}", // TODO show percent to next (${%}%)
        "",
        s"${WHITE}Unlocked passives:"
      ) ++ levelPassivesView(level)
    ) { _ => noop }
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

  private def combatClassButton(
      player: Player
  )(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = {
    val combatClass = player.combatClass
    val name = combatClass.map { _.name }.getOrElse("Wayfarer")
    val prefix = if (name(0).isVowel) "an" else "a"
    Button(
      combatClass.map { _.mainHands.head }.getOrElse(FlowerVariant.DANDELION),
      "Combat Class",
      List(
        s"You are fighting as $prefix $YELLOW$name",
        "",
        s"${WHITE}Abilities:"
      ) ++ abilitiesView(combatClass)
        ++ List("", "[Click] to change your combat class")
    ) { _ => updateMenu(CombatClassMenu.apply) }
  }

  private def racialPassiveView(
      player: Player,
      team: Team,
      combatClass: Option[CombatClass]
  ): List[String] = (team.id, combatClass.map { _.id }.getOrElse("")) match {
    // Warrior
    case (Dwarves, Berserker | Guardian) => (
      s"${YELLOW}Sturdiness"
        :: "You take up to 25% less damage depending"
        :: "on how much damage you've already taken."
        :: Nil
    )
    case (Elves, Berserker | Guardian) => (
      s"${YELLOW}Blade Dance"
        :: "Every 3rd outgoing melee attack you'll dodge"
        :: "the first incoming melee attack for 3 seconds."
        :: Nil
    )
    case (Humans, Berserker | Guardian) => (
      s"${YELLOW}Last Stand"
        :: "Upon receiving a fatal blow you instead"
        :: "become invulnerable for 3 seconds."
        :: Nil
    )
    case (Orcs, Berserker | Guardian) => (
      s"${YELLOW}Bloodlust"
        :: "While below half health your crit chance"
        :: "is doubled."
        :: Nil
    )

    // Hunter
    case (_, Marksman | Ranger) => (
      s"${YELLOW}Hunter Pet"
        :: "Your pet fights by your side, attacking"
        :: "everything you're attacking."
        :: Nil
    )

    // Mage
    case (Dwarves, Elementalist | Wizard) => (
      s"${YELLOW}Unstable Bunker"
        :: "When falling below half health and"
        :: "after a short delay you damage and push"
        :: "nearby enemies back."
        :: Nil
    )
    case (Elves, Elementalist | Wizard) => (
      s"${YELLOW}Regrowth"
        :: Nil
    )
    case (Humans, Elementalist | Wizard) => (
      s"${YELLOW}Arcane Barrier"
        :: "When falling below half health you"
        :: "receive an arcane shield."
        :: Nil
    )
    case (Orcs, Elementalist | Wizard) => (
      s"${YELLOW}Flame Totem"
        :: "When falling below 75% health you"
        :: "summon a totem to attack nearby enemies."
        :: Nil
    )

    // Default
    case _ => s"${GRAY}None" :: Nil
  }

  private def factionButton(
      player: Player
  )(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = {
    val team = player.team.getOrElse(LegendsTeam.Wayfarers.instance)
    val combatClass = player.combatClass
    Button(
      ItemType.ARMOR_STAND,
      "Faction",
      List(
        s"You are fighting for the $YELLOW${team.name}",
        "",
        s"${WHITE}Racial passive:"
      ) ++ racialPassiveView(player, team, combatClass)
        ++ List("", "[Click] to change factions")
    ) { _ => updateMenu(FactionMenu.apply) }
  }

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

  private def returnButton(player: Player)(implicit
      spawnService: SpawnService
  ): Button = {
    val cooldown = spawnService.cooldowns.getOrElse(player.id, 0L)
    val deltaSeconds = (cooldown - System.currentTimeMillis) / 1000
    val cooldownText =
      if (deltaSeconds > 0) {
        if (deltaSeconds < 60) s"$RED$deltaSeconds seconds" :: Nil
        else s"$RED${deltaSeconds / 60} minutes" :: Nil
      } else List(
        "[Click] To warp to your spawn",
        s"or execute the $YELLOW/warp ${FormatCode.RESET}command"
      )

    Button(
      ItemType.FLINT,
      "Return to Spawn",
      List(
        "A pitch black splinter that is said to have",
        "been part of The Monolith. It can take you",
        "back to where The Monolith's power was once",
        "at its strongest.",
        ""
      ) ++ cooldownText
    ) { _ =>
      if (spawnService.warpToSpawn(player.id)) closeMenu
      else noop
    }
  }

  private def helpButton(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = Button(
    ItemType.BOOK,
    "Help Menu",
    "Read the Server Manual or",
    "contact a member of staff",
    "if you have any more questions.",
    "",
    "[Click] To open the Help Menu"
  ) { _ => updateMenu(HelpMenu.apply) }

  private def dailyLoginButton(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = Button(
    ItemType.CLOCK,
    "Daily Login",
    "Every day you get a reward",
    "just for logging in! The more",
    "often you log in, the greater",
    "the rewards. And don't worry;",
    "missing a day or two does not",
    "reset your progress.",
    "",
    "[Click] To view your reward calendar"
  ) { _ => updateMenu(DailyLoginMenu.apply) }

  private def dailyVoteButton(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = Button(
    ItemType.PAPER,
    "Daily Votes",
    "Get even more daily rewards by voting",
    "for WarCraft! For every vote cast you",
    s"receive a ${YELLOW}Vote Crate${FormatCode.RESET}.",
    "",
    "You can vote on:",
    "minecraft-server-list.com",
    "minecraftservers.org",
    "minecraft-server.net",
    "minecraft-mp.com",
    "minecraftservers.biz",
    "",
    "[Click] To paste the links in your chat",
    s"or execute the $YELLOW/vote ${FormatCode.RESET}command"
  ) { click =>
    commandService.executeCommand(click.player.id, "vote")
    noop
  }

  private def challengesButton(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = Button(
    ItemType.TNT,
    "Challenges",
    "Repeatable tasks that reward",
    "you for helping your faction's",
    "efforts on Aldmore.",
    "",
    "[Click] To view your challenges"
  ) { _ => updateMenu(ChallengesMenu.apply) }

  private def collectiblesButton(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = Button(
    ItemType.BOOKSHELF,
    "Collectibles",
    "From house decorations to",
    "mini pets, there's a lot",
    "to collect on Aldmore.",
    "",
    "[Click] To view your collectibles"
  ) { _ => updateMenu(CollectiblesMenu.apply) }

  private def leaderboardsButton(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = Button(
    ItemType.BANNER,
    "Leaderboards",
    "Your actions on the server do",
    "not go unnoticed. The Titans keep",
    "keep records of all the legends",
    "forged on Aldmore and reward those",
    "that are the greatest!",
    "",
    "[Click] To view the leaderboards"
  ) { _ => updateMenu(LeaderboardsMenu.apply) }

  private def shopButton(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Button = Button(
    ItemType.GOLDEN_APPLE,
    "WarCraft: Shop",
    "To go even further beyond in supporting the",
    "server the WarCraft: Shop has 12 different",
    "items for sale every day. These items can",
    "be bought with Fragments which are available",
    s"from the web shop at ${YELLOW}shop.warcraft.gg",
    "",
    "[Click] To view the shop"
  ) { _ => updateMenu(ShopMenu.apply) }

  def apply(player: Player)(implicit
      menuService: MenuService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      legendsDataService: LegendsPlayerDataService,
      loginRewardService: LoginCalendarService,
      serverDataService: ServerDataService,
      challengeService: ChallengeService,
      leaderboardService: LeaderboardService,
      shopService: ShopService,
      shopProfileService: ShopProfileService,
      itemService: ItemService,
      miniPetFactory: MiniPetFactory,
      commandService: CommandService,
      spawnService: SpawnService,
      teamService: TeamService
  ): Menu = Menu(
    "WarCraft: Legends",
    MenuSize.TWO_ROWS,
    // 1st row
    0 -> playerButton(player),
    1 -> levelButton(player),
    2 -> combatClassButton(player),
    3 -> factionButton(player),
    // 4
    // TODO 5 -> itemStorageButton(player),
    6 -> returnButton(player),
    // 7
    8 -> helpButton,
    // 2nd row
    9 -> dailyLoginButton,
    10 -> dailyVoteButton,
    11 -> challengesButton,
    // 12
    // 13
    14 -> collectiblesButton,
    15 -> leaderboardsButton,
    // 16
    17 -> shopButton
  )
}
