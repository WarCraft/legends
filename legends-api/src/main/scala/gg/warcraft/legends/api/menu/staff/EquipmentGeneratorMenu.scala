/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu.staff

import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.combat.LegendsCombatArchetype._
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.equipment.{EquipmentGenerator, EquipmentType}
import gg.warcraft.legends.api.item.equipment.EquipmentType._
import gg.warcraft.monolith.api.block.variant.{MobHeadVariant, WoolVariant}
import gg.warcraft.monolith.api.item.ItemType
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object EquipmentGeneratorMenu {
  private def generate(player: Player, archetype: LegendsCombatArchetype)(implicit
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Unit = {
    val item = equipmentGenerator.generate(archetype, LegendsTeam.random)
    playerService.giveItem(player.id, item)
  }

  private def generate(
      player: Player,
      archetype: LegendsCombatArchetype,
      typ: EquipmentType
  )(implicit
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Unit = {
    val item = equipmentGenerator.generate(archetype, LegendsTeam.random, typ)
    playerService.giveItem(player.id, item)
  }

  private def build(player: Player, archetype: LegendsCombatArchetype)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Unit = {
    val menu = EquipmentGeneratorTeamMenu(archetype)
    // TODO menuService.showMenu(player.id, menu)
  }

  def warriorButton(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Button = Button(
    ItemType.SWORD,
    "Warrior",
    "[Click] to generate a Warrior item"
      :: "[Right Click] to build a Warrior item"
      :: Nil
  ) { click =>
    if (click.isLeft) generate(click.player, WARRIOR)
    else build(click.player, WARRIOR)
  }

  def hunterButton(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Button = Button(
    ItemType.BOW,
    "Hunter",
    "[Click] to generate a Hunter item"
      :: "[Right Click] to build a Hunter item"
      :: Nil
  ) { click =>
    if (click.isLeft) generate(click.player, HUNTER)
    else build(click.player, HUNTER)
  }

  def mageButton(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Button = Button(
    ItemType.HOE,
    "Mage",
    "[Click] to generate a Mage item"
      :: "[Right Click] to build a Mage item"
      :: Nil
  ) { click =>
    if (click.isLeft) generate(click.player, MAGE)
    else build(click.player, MAGE)
  }

  def headButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def chestButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def legsButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def feetButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def swordButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def shieldButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def axeButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def skullButton(implicit
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Button = Button(
    MobHeadVariant.SKELETON,
    "Skull",
    "[Click] to generate a Skull item"
      :: "[Right Click] to build a Skull item"
      :: Nil
  ) { click =>
    if (click.isLeft) generate(click.player, WARRIOR, SKULL)
  // else build(click.player, WARRIOR, SKULL)
  }

  def bowButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def crossbowButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def staffButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def wandButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def bookButton: Button = Button(
    ItemType.PLANKS,
    "Placeholder",
    Nil
  ) { _ => () }

  def apply(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Menu = Menu(
    "Equipment Generator",
    MenuSize.TWO_ROWS,
    Map(
      // 1st row
      0 -> warriorButton,
      1 -> hunterButton,
      2 -> mageButton,
      4 -> headButton,
      5 -> chestButton,
      6 -> legsButton,
      7 -> feetButton,
      8 -> Button.back(StaffMenu.apply),
      // 2nd row
      9 -> swordButton,
      10 -> shieldButton,
      11 -> axeButton,
      12 -> skullButton,
      13 -> bowButton,
      14 -> crossbowButton,
      15 -> staffButton,
      16 -> wandButton,
      17 -> bookButton
    )
  )
}

object EquipmentGeneratorTeamMenu {
  private def generate(player: Player, archetype: LegendsCombatArchetype)(implicit
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Unit = {
    val item = equipmentGenerator.generate(archetype, LegendsTeam.random)
    playerService.giveItem(player.id, item)
  }

  private def build(player: Player, archetype: LegendsCombatArchetype)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Unit = {
    val menu = EquipmentGeneratorTeamMenu(archetype)
    menuService.showMenu(player.id, menu)
  }

  def dwarvenButton(archetype: LegendsCombatArchetype): Button = Button(
    WoolVariant.ORANGE,
    "Dwarven",
    s"[Click] to generate a Dwarven $archetype item"
      :: s"[Right Click] to build a Dwarven $archetype item"
      :: Nil
  ) { _ => () }

  def elvenButton(archetype: LegendsCombatArchetype): Button = Button(
    WoolVariant.GREEN,
    "Elven",
    s"[Click] to generate an Elven $archetype item"
      :: s"[Right Click] to build an Elven $archetype item"
      :: Nil
  ) { _ => () }

  def humanButton(archetype: LegendsCombatArchetype): Button = Button(
    WoolVariant.CYAN,
    "Human",
    s"[Click] to generate a Human $archetype item"
      :: s"[Right Click] to build a Human $archetype item"
      :: Nil
  ) { _ => () }

  def orcishButton(archetype: LegendsCombatArchetype): Button = Button(
    WoolVariant.RED,
    "Orcish",
    s"[Click] to generate a Orcish $archetype item"
      :: s"[Right Click] to build an Orcish $archetype item"
      :: Nil
  ) { _ => () }

  def apply(archetype: LegendsCombatArchetype)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Menu = Menu(
    "Equipment Generator",
    MenuSize.ONE_ROW,
    Map(
      0 -> dwarvenButton(archetype),
      1 -> elvenButton(archetype),
      2 -> humanButton(archetype),
      3 -> orcishButton(archetype),
      8 -> Button.back(_ => EquipmentGeneratorMenu.apply)
    )
  )
}

object EquipmentGeneratorTypeMenu {
  def apply(): Menu = {
    ???
  }
}
