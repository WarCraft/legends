/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu.staff

import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.combat.LegendsCombatArchetype._
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.monolith.api.block.variant.WoolVariant
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

  private def build(player: Player, archetype: LegendsCombatArchetype)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Unit = {
    val menu = EquipmentGeneratorTeamMenu(archetype)
    // TODO menuService.showMenu(player.id, menu)
  }

  def warriorButton()(implicit
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

  def hunterButton()(implicit
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

  def mageButton()(implicit
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

  def apply(implicit
      menuService: MenuService,
      playerService: PlayerService,
      equipmentGenerator: EquipmentGenerator
  ): Menu = Menu(
    "Equipment Generator",
    MenuSize.ONE_ROW,
    Map(
      0 -> warriorButton,
      1 -> hunterButton,
      2 -> mageButton
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
