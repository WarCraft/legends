/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.entity._
import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.item.crate.LootCrateService
import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityFatalDamageEvent
import gg.warcraft.monolith.api.item.ItemService
import gg.warcraft.monolith.api.player.currency.CurrencyService
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.siege.api.objective.ObjectiveCaptureEvent

class CaptureAndKillRewardHandler(config: LegendsConfig)(implicit
    playerService: PlayerService,
    currencyService: CurrencyService,
    combatDataService: CombatDataService,
    itemService: ItemService,
    lootCrateService: LootCrateService
) extends Event.Handler {
  private val killFavor = config.playerKillFavor
  private val killMessage = Message.server(s"You gained $killFavor Favor.")

  private val rewards =
    config.objectiveCaptureRewards.map { case (objective, rewards) =>
      objective -> lootCrateService.parseRewards(rewards)
    }

  override def handle(event: Event): Unit = event match {
    case EntityFatalDamageEvent(entity: Player, _) =>
      val attackers = entity.combatData.attackedBy.toList
      currencyService.addCurrency(
        LegendsCurrency.Favor,
        killFavor,
        attackers: _*
      )
      attackers.foreach { playerService.sendMessage(_, killMessage) }

    case ObjectiveCaptureEvent(objective, _, attackers) =>
      lazy val attackerIds = attackers.iterator.map { _.id }.toList

      val favor = config.objectiveTallyPoints(objective.typ)
      if (favor > 0) {
        currencyService.addCurrency(LegendsCurrency.Favor, favor, attackerIds: _*)
        val message = Message.server(s"You gained $favor Favor.")
        attackerIds.foreach { playerService.sendMessage(_, message) }
      }

      rewards.get(objective.id) match {
        case Some(crates) => crates.foreach { crate =>
            val message =
              if (crate.count == 1) Message.server(s"You received 1 ${crate.name}.")
              else Message.server(s"You received ${crate.count} ${crate.name}s.")
            attackerIds.foreach { attackerId =>
              itemService.giveTo(attackerId, crate)
              playerService.sendMessage(attackerId, message)
            }
          }
        case None =>
      }

    case _ =>
  }
}
