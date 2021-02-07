/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity.EntityOps
import gg.warcraft.legends.api.combat.LegendsCombatClass.{
  Berserker, Elementalist, Guardian, Marksman, Ranger, Wizard
}
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute.{
  Dexterity, Intellect, Strength
}
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder
import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.{ColorCode, Message}
import gg.warcraft.monolith.api.item.ItemType._
import gg.warcraft.monolith.api.item.{Item, ItemService}
import gg.warcraft.monolith.api.menu.Button.noop
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuSize}
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}

object WeaponVendorDialog {
  private final val price = 0

  private final val infoButton: Button = Button.noop(
    BOOK,
    "Weapon Vendor"
  )

  private def weaponButton(item: Item, player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService
  ): Button = Button(
    item.`type`,
    item.name,
    s"${ColorCode.AQUA}[Shift Right Click] To buy a ${item.name}",
    s"${ColorCode.AQUA}for $price gold"
  ) { click =>
    if (click.isShiftRight) {
      if (player.gold < price) {
        val message =
          Message.server(s"You need at least $price gold to purchase this item.")
        player.sendMessage(message)
      } else if (!player.inventory.hasSpace(1)) {
        val message =
          Message.server(
            "Please make sure you have at least 1 free inventory slot."
          )
        player.sendMessage(message)
      } else {
        currencyService.removeCurrency(LegendsCurrency.Gold, price, player.id)
        itemService.giveTo(player.id, item)
      }
    }
    noop
  }

  def apply(player: Player)(implicit
      currencyService: CurrencyService,
      itemService: ItemService,
      playerService: PlayerService,
      combatDataService: CombatDataService
  ): Menu = {
    val data = player.combatClass match {
      case Some(combatClass) =>
        combatClass.id match {
          case Berserker    => Some((AXE, "Basic Axe", Strength, 1))
          case Guardian     => Some((SWORD, "Basic Sword", Strength, 1))
          case Marksman     => Some((CROSSBOW, "Basic Crossbow", Dexterity, 100))
          case Ranger       => Some((BOW, "Basic Bow", Dexterity, 100))
          case Elementalist => Some((HOE, "Basic Staff", Intellect, 1))
          case Wizard       => Some((STICK, "Basic Wand", Intellect, 1))
          case _            => None
        }
      case _ => None
    }

    var weaponButtons = Map.empty[Int, Button]
    val weapon = data.map { data =>
      val builder = new EquipmentBuilder(data._1, ItemRarity.Common, data._2)
      builder.addAttribute(data._3, 1)
        .withCustomModelData(data._4)
        .build()
    }
    weapon.foreach { it => weaponButtons += (2 -> weaponButton(it, player)) }

    Menu(
      "Weapon Vendor",
      MenuSize.ONE_ROW,
      Map(
        0 -> infoButton
      ) ++ weaponButtons
    )
  }
}
