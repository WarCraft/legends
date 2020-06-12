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
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.player.level.LevelService
import gg.warcraft.legends.api.util.legendsplayer._
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.item.{ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.item.variant.AxeVariant
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.util.TimeUtils
import gg.warcraft.monolith.api.util.string._

object CombatClassMenu {
  def abilitiesView(combatClass: CombatClass): List[String] = List(
    "Abilities:",
    s"[Left Click]$YELLOW${combatClass.leftClick.getOrElse("Basic Attack")}",
    s"[Right Click]$YELLOW${combatClass.rightClick.getOrElse("Basic Attack")}",
    s"[Shift Left Click]$YELLOW${combatClass.shiftLeftClick.getOrElse("Basic Attack")}",
    s"[Shift Right Click]$YELLOW${combatClass.shiftRightClick.getOrElse("Basic Attack")}"
  )

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
      combatClass: CombatClass,
      icon: ItemTypeOrVariant,
      cooldown: Option[String],
      lore: String*
  )(implicit
      combatDataService: CombatDataService
  ): Button = {
    // TODO val combatClassShopId = ???
    // TODO val isLocked = !shopProfileService.getProfile(player.id).hasCombatClass(combatClassShopId);
    val canSwitch = player.combatClass match {
      case Some(playerCombatClass) => playerCombatClass != combatClass
      case None                    => true
    }

    val isLocked = true // TODO

    val tooltip = lore ++ List("") ++ abilitiesView(combatClass) ++ List(
      ""
    ) ++ switchView(combatClass, isLocked, canSwitch, cooldown)
    Button(
      icon,
      combatClass.name,
      tooltip.toList
    ) { click =>
      if (!isLocked && cooldown.isEmpty && click.isShiftRight) {
        val newCombatData =
          player.combatData.copy(combatClass =
            Some(combatClass)
          ) // TODO does this belong in a CombatClassService which then calls combat data?
        combatDataService.setCombatData(newCombatData)
      }
    }
  }

  def guardianButton(
      player: Player,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = {
    val combatClass = combatClassService.combatClasses(Guardian.name)
    combatClassButton(
      player,
      combatClass,
      ItemType.SHIELD,
      cooldown,
      "Guardians wield a sword and a shield.",
      "Their skills focus on control and defense.",
      "",
      s"$YELLOW[Left Click] Melee",
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
  }

  def berserkerButton(
      player: Player,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = {
    val combatClass = combatClassService.combatClasses(Berserker.name)
    combatClassButton(
      player,
      combatClass,
      AxeVariant.STONE,
      cooldown,
      "Berserkers dual wield axes. Their skills",
      "focus on damage and recovering health.",
      "",
      s"$YELLOW[Left Click] Melee",
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
  }

  def marksmanButton(
      player: Player,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = {
    val combatClass = combatClassService.combatClasses(Marksman.name)
    combatClassButton(
      player,
      combatClass,
      ItemType.CROSSBOW,
      cooldown,
      "Marksmen use a crossbow. Their skills",
      "focus on damage while sacrificing mobility.",
      "",
      s"$YELLOW[Left Click] Power Shot",
      "Fire a high velocity shot dealing damage",
      "and knocking back your target.",
      s"$YELLOW[Right Click] Shoot",
      "A normal Minecraft bow shot.",
      s"$YELLOW[Shift Left Click] Focus",
      "Focus your vision and greatly increase",
      "your damage while becoming immobile",
      "(cast again to cancel).",
      s"$YELLOW[Shift Right Click] Sneak",
      "Gain a speed boost and hide in plain",
      "sight, becoming invisible to enemies."
    )
  }

  def rangerButton(
      player: Player,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = {
    val combatClass = combatClassService.combatClasses(Ranger.name)
    combatClassButton(
      player,
      combatClass,
      ItemType.BOW,
      cooldown,
      "Hunters use bow and arrow. Their skills",
      "offer damage, control, and mobility.",
      "",
      s"$YELLOW[Left Click] Toxic Arrow",
      "Fire a poisoned arrow damaging your target",
      "and applying poison for a short duration.",
      s"$YELLOW[Right Click] Shoot",
      "A normal Minecraft bow shot.",
      s"$YELLOW[Shift Left Click] Trap",
      "Place a trap at your feet which roots",
      "any enemies that trigger it.",
      s"$YELLOW[Shift Right Click] Disengage",
      "Leap backwards a sizable distance."
    )
  }

  def elementalistButton(
      player: Player,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = {
    val combatClass = combatClassService.combatClasses(Elementalist.name)
    combatClassButton(
      player,
      combatClass,
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
  }

  def wizardButton(
      player: Player,
      cooldown: Option[String]
  )(implicit
      combatDataService: CombatDataService,
      combatClassService: CombatClassService
  ): Button = {
    val combatClass = combatClassService.combatClasses(Wizard.name)
    combatClassButton(
      player,
      combatClass,
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
  }

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
      legendsDataService: LegendsPlayerDataService
  ): Menu = {
    val combatClass = player.combatClass
    val combatClassName = combatClass.get.name
    val combatClassPrefix = if (combatClassName(0).isVowel) "an" else "a"

    val cooldown = player.combatClassCooldown
    val cooldownText = // TODO add utils.string implicit ops in Monolith
      if (cooldown <= System.currentTimeMillis) None
      else Some(TimeUtils.timeUntil(cooldown))

    val infoButton = Button(
      ItemType.BOOK,
      "Choosing a Combat Class",
      List(
        s"You are currently fighting as $combatClassPrefix",
        s"$YELLOW$combatClassName",
        "",
        "You can change your combat class",
        "once every 10 minutes."
      )
    ) { _ => () }

    Menu(
      "Combat Class",
      MenuSize.ONE_ROW,
      Map(
        0 -> infoButton,
        8 -> Button.back(MainMenu.apply),
        // 1st row
        2 -> guardianButton(player, cooldownText),
        3 -> berserkerButton(player, cooldownText),
        4 -> marksmanButton(player, cooldownText),
        5 -> rangerButton(player, cooldownText),
        6 -> elementalistButton(player, cooldownText),
        7 -> wizardButton(player, cooldownText)
      )
    )
  }
}
