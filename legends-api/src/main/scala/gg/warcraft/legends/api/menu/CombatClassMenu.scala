/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu

import gg.warcraft.combat.api.combatclass.{CombatClass, CombatClassService}
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.combat.LegendsCombatClass._
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.shop.profile.{ShopProfile, ShopProfileService}
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.variant.AxeVariant
import gg.warcraft.monolith.api.item.{ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.menu.Button.{closeMenu, noop}
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.util.TimeUtils
import gg.warcraft.monolith.api.util.string._

object CombatClassMenu {
  private def infoButton(combatClass: Option[CombatClass]): Button = {
    val name = combatClass.map { _.name }.getOrElse("Adventurer")
    val prefix = if (name(0).isVowel) "an" else "a"
    Button.noop(
      ItemType.BOOK,
      "Choosing a Combat Class",
      s"You are currently fighting as $prefix",
      s"$YELLOW$name",
      "",
      "You can change your combat class",
      "once every 10 minutes."
    )
  }

  def switchView(
      combatClass: CombatClass,
      isLocked: Boolean,
      canSwitch: Boolean,
      cooldown: Option[String]
  ): List[String] = {
    if (isLocked) {
      List(s"${RED}Locked")
    } else if (canSwitch) {
      if (cooldown.isEmpty)
        List(s"$GREEN[Shift Right Click] To switch to ${combatClass.name}")
      else List(s"${RED}You can change combat class in ${cooldown.get}")
    } else {
      List("This is your current combat class")
    }
  }

  private def combatClassButton(
      player: Player,
      shopProfile: ShopProfile,
      combatClass: CombatClass,
      icon: ItemTypeOrVariant,
      cooldown: Option[String],
      lore: String*
  )(implicit
      combatDataService: CombatDataService
  ): Button = {
    val isLocked = shopProfile.unlocked.contains(combatClass.id)
    val canSwitch = player.combatClass match {
      case Some(playerCombatClass) => playerCombatClass != combatClass
      case None                    => true
    }
    val tooltip =
      lore ++ List("") ++ switchView(combatClass, isLocked, canSwitch, cooldown)
    Button(
      icon,
      combatClass.name,
      tooltip.toList
    ) { click =>
      if (!isLocked && cooldown.isEmpty && click.isShiftRight) {
        combatDataService.setCombatClass(player.id, Some(combatClass))
        closeMenu
      } else noop
    }
  }

  def guardianButton(
      player: Player,
      shopProfile: ShopProfile,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = combatClassButton(
    player,
    shopProfile,
    combatClassService.combatClasses(Guardian),
    ItemType.SHIELD,
    cooldown,
    "Guardians wield a sword and a shield.",
    "Their skills focus on control and defense.",
    "",
    s"$YELLOW[Left Click] Basic Attack",
    "A normal Minecraft melee swing.",
    s"$YELLOW[Right Click] Hamstring",
    "Damage your enemy and slow them for a",
    "short duration.",
    s"$YELLOW[Shift Left Click] Slam",
    "An overpowering blow that damages the",
    "target, knocks them back, and makes",
    "them nauseous for a moderate duration.",
    s"$YELLOW[Shift Right Click] Stand Your Ground",
    "Bunker down to greatly increase all of your",
    "resistances for a moderate duration."
  )

  def berserkerButton(
      player: Player,
      shopProfile: ShopProfile,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = combatClassButton(
    player,
    shopProfile,
    combatClassService.combatClasses(Berserker),
    AxeVariant.STONE,
    cooldown,
    "Berserkers dual wield axes. Their skills",
    "focus on damage and recovering health.",
    "",
    s"$YELLOW[Left Click] Basic Attack",
    "A normal Minecraft melee swing.",
    s"$YELLOW[Right Click] Maul",
    "Maul your enemy for damage and receive",
    "absorption for a short duration.",
    s"$YELLOW[Shift Left Click] Grudge",
    "Mark a player with your grudge for a",
    "moderate duration. Any attacks made",
    "against them heal you a little bit.",
    s"$YELLOW[Shift Right Click] Shatter",
    "Slam down into the ground in front of",
    "you, damaging enemies while throwing",
    "them into the air and making them",
    "nauseous for a moderate duration."
  )

  def marksmanButton(
      player: Player,
      shopProfile: ShopProfile,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = combatClassButton(
    player,
    shopProfile,
    combatClassService.combatClasses(Marksman),
    ItemType.CROSSBOW,
    cooldown,
    "Marksmen use a crossbow. Their skills",
    "focus on damage while sacrificing mobility.",
    "",
    s"$YELLOW[Left Click] Power Shot",
    "Fire a high velocity shot dealing damage",
    "and knocking back your target.",
    s"$YELLOW[Right Click] Basic Attack",
    "A normal Minecraft bow shot.",
    s"$YELLOW[Shift Left Click] Focus",
    "Focus your vision and greatly increase",
    "your damage while becoming immobile",
    "(cast again to cancel).",
    s"$YELLOW[Shift Right Click] Sneak",
    "Gain a speed boost and hide in plain",
    "sight, becoming invisible to enemies."
  )

  def rangerButton(
      player: Player,
      shopProfile: ShopProfile,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = combatClassButton(
    player,
    shopProfile,
    combatClassService.combatClasses(Ranger),
    ItemType.BOW,
    cooldown,
    "Hunters use bow and arrow. Their skills",
    "offer damage, control, and mobility.",
    "",
    s"$YELLOW[Left Click] Toxic Arrow",
    "Fire a poisoned arrow damaging your target",
    "and applying poison for a short duration.",
    s"$YELLOW[Right Click] Basic Attack",
    "A normal Minecraft bow shot.",
    s"$YELLOW[Shift Left Click] Trap",
    "Place a trap at your feet which roots",
    "any enemies that trigger it.",
    s"$YELLOW[Shift Right Click] Disengage",
    "Leap backwards a sizable distance."
  )

  def elementalistButton(
      player: Player,
      shopProfile: ShopProfile,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = combatClassButton(
    player,
    shopProfile,
    combatClassService.combatClasses(Elementalist),
    ItemType.HOE,
    cooldown,
    "Elementalists wield a staff (hoe). Their",
    "skills offer large area damage.",
    "", // TODO refactor these skill descriptions into combat class config
    s"$YELLOW[Left Click] Fireball",
    "Fling a ball of fire at your target",
    "dealing damage and setting them on fire",
    s"$YELLOW[Right Click] Bolt",
    "Call down lightning at your targeted",
    "location damaging nearby enemies.",
    s"$YELLOW[Shift Left Click] Hail Blast",
    "Blast an icy torrent in front of you",
    "damaging and slowing enemies.",
    s"$YELLOW[Shift Right Click] Molten Storm",
    "Conjure a storm of molten rocks at your",
    "targeted location. Each fireball damages",
    "and briefly sets enemies on fire."
  )

  def wizardButton(
      player: Player,
      shopProfile: ShopProfile,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = combatClassButton(
    player,
    shopProfile,
    combatClassService.combatClasses(Wizard),
    ItemType.STICK,
    cooldown,
    "Wizards wield wand (stick) and book. Their",
    "skills focus on area control, and mobility.",
    "",
    s"$YELLOW[Left Click] Fireball",
    "Fling a ball of fire at your target",
    "dealing damage and setting them on fire",
    s"$YELLOW[Right Click] Arcane Missile",
    "Cast an arcane missile damaging nearby",
    "enemies on impact.",
    s"$YELLOW[Shift Left Click] Teleport",
    "Teleport to your targeted location, releasing",
    "an arcane explosion around you on arrival",
    "damaging and slowing nearby enemies.",
    s"$YELLOW[Shift Right Click] Mass Piggify",
    "Conjure chaotic energies at your targeted",
    "location, which after a short delay force",
    "your enemies to ride uncontrollable pigs",
    "making them nauseous."
  )

  def apply(
      player: Player
  )(implicit
      menuService: MenuService,
      playerService: PlayerService,
      combatDataService: CombatDataService,
      combatClassService: CombatClassService,
      attributeService: AttributeService,
      currencyService: CurrencyService,
      levelService: LevelService,
      legendsDataService: LegendsPlayerDataService,
      shopProfileService: ShopProfileService
  ): Menu = {
    val shopProfile = shopProfileService.profile(player.id)
    val cooldown = player.combatClassCooldown
    val cooldownText = // TODO add utils.string implicit ops in Monolith
      if (cooldown <= System.currentTimeMillis) None
      else Some(TimeUtils.timeUntil(cooldown))
    Menu(
      "Combat Class",
      MenuSize.TWO_ROWS,
      0 -> infoButton(player.combatClass),
      9 -> Button.back(MainMenu.apply),
      // 1st row
      2 -> guardianButton(player, shopProfile, cooldownText),
      3 -> berserkerButton(player, shopProfile, cooldownText),
      4 -> marksmanButton(player, shopProfile, cooldownText),
      5 -> rangerButton(player, shopProfile, cooldownText),
      6 -> elementalistButton(player, shopProfile, cooldownText),
      7 -> wizardButton(player, shopProfile, cooldownText)
    )
  }
}
