/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity.EntityOps
import gg.warcraft.legends.api.combat.LegendsCombatClass._
import gg.warcraft.legends.api.entity.attribute.LegendsAttribute._
import gg.warcraft.legends.api.item.ItemRarity
import gg.warcraft.monolith.api.core.auth.Principal
import gg.warcraft.monolith.api.core.command.Command
import gg.warcraft.monolith.api.item.{ItemService, ItemType}
import gg.warcraft.monolith.api.player.{Player, PlayerService}

class KitCommand extends Command("kit")

object KitCommand {
  class Handler(implicit
      playerService: PlayerService,
      combatDataService: CombatDataService,
      itemService: ItemService
  ) extends Command.Handler {
    override def handle(
        executor: Principal,
        command: Command,
        args: String*
    ): Command.Result = executor match {
      case player: Player =>
        val data = player.combatClass match {
          case Some(combatClass) =>
            combatClass.id match {
              case Berserker    => Some((ItemType.AXE, "Basic Axe", Strength))
              case Guardian     => Some((ItemType.SWORD, "Basic Sword", Strength))
              case Marksman     => Some((ItemType.BOW, "Basic Bow", Dexterity))
              case Ranger       => Some((ItemType.BOW, "Basic Bow", Dexterity))
              case Elementalist => Some((ItemType.HOE, "Basic Staff", Intellect))
              case Wizard       => Some((ItemType.STICK, "Basic Wand", Intellect))
              case _            => None
            }
          case _ => None
        }

        data match {
          case Some(data) =>
            val builder = new EquipmentBuilder(data._1, ItemRarity.Common, data._2)
            builder.addAttribute(data._3, 1)
            playerService.giveItem(player.id, builder.build())
            Command.success
          case None => Command.invalid
        }

      case _ => Command.playersOnly
    }
  }
}
