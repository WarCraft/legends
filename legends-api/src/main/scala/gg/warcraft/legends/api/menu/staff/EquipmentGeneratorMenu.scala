/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.menu.staff

import gg.warcraft.combat.api.combatclass.{CombatClass, CombatClassService}
import gg.warcraft.legends.api.combat.LegendsCombatClass._
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.item.crate.LootCrateService
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator
import gg.warcraft.legends.api.item.equipment.config.EquipmentConfigRepository
import gg.warcraft.monolith.api.entity.team.TeamService
import gg.warcraft.monolith.api.item.{ItemService, ItemType, ItemTypeOrVariant}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuService, MenuSize}
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object EquipmentGeneratorMenu {
  private def combatClassButton(
      icon: ItemTypeOrVariant,
      combatClass: CombatClass
  )(implicit
      menuService: MenuService,
      playerService: PlayerService,
      generator: EquipmentGenerator,
      teamService: TeamService
  ): Button = Button(
    icon,
    combatClass.name,
    s"[Click] to generate a ${combatClass.name} item"
      :: s"[Right Click] to build a ${combatClass.name} item"
      :: Nil
  ) { click =>
    if (click.isLeft) {
      val item = generator.generate(combatClass, LegendsTeam.random)
      playerService.giveItem(click.player.id, item)
      noop
    } else noop
  }

  def apply(player: Player)(implicit
      menuService: MenuService,
      playerService: PlayerService,
      itemService: ItemService,
      equipmentConfigRepository: EquipmentConfigRepository,
      combatClassService: CombatClassService,
      teamService: TeamService,
      lootCrateService: LootCrateService
  ): Menu = {
    implicit val generator: EquipmentGenerator = new EquipmentGenerator()
    Menu(
      "Equipment Generator",
      MenuSize.TWO_ROWS,
      // 1st row
      0 -> combatClassButton(ItemType.AXE, Berserker.instance),
      1 -> combatClassButton(ItemType.SWORD, Guardian.instance),
      2 -> combatClassButton(ItemType.BOW, Marksman.instance),
      3 -> combatClassButton(ItemType.BOW, Ranger.instance),
      4 -> combatClassButton(ItemType.HOE, Elementalist.instance),
      5 -> combatClassButton(ItemType.STICK, Wizard.instance),
//      4 -> headButton, TODO
//      5 -> chestButton,
//      6 -> legsButton,
//      7 -> feetButton,
      8 -> Button.back(StaffMenu.apply)
      // 2nd row TODO
//      9 -> swordButton,
//      10 -> shieldButton,
//      11 -> axeButton,
//      12 -> skullButton,
//      13 -> bowButton,
//      14 -> crossbowButton,
//      15 -> staffButton,
//      16 -> wandButton,
//      17 -> bookButton
    )
  }
}
