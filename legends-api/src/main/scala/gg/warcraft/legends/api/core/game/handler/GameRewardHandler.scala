/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.game.handler

import gg.warcraft.legends.api.LegendsConfig
import gg.warcraft.legends.api.core.game.{
  GameService, SkirmishFinishedEvent, WarFinishedEvent
}
import gg.warcraft.legends.api.item.crate.LootCrateService
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.entity.EntityFatalDamageEvent
import gg.warcraft.monolith.api.player.{Player, PlayerService}
import gg.warcraft.siege.api.objective.{
  ObjectiveCaptureEvent, ObjectiveNeutralizeEvent
}

class GameRewardHandler(config: LegendsConfig)(implicit
    playerService: PlayerService,
    lootCrateService: LootCrateService,
    gameService: GameService
) extends Event.Handler {
  private val winnerCrate = lootCrateService.crates("WAR_CRATE").withCount(5)
  private val loserCrate = lootCrateService.crates("WAR_CRATE").withCount(2)

  override def handle(event: Event): Unit = event match {
    case ObjectiveCaptureEvent(objective, attackers, attackerIds)
        if objective.typ != "TUTORIAL" =>
      gameService.captureObjective(objective, attackers)
    // TODO give attackers capture crates

    case ObjectiveNeutralizeEvent(objective, _, _, _, _)
        if objective.typ != "TUTORIAL" =>
      gameService.neutralizeObjective(objective)

    case EntityFatalDamageEvent(target: Player, damage) =>
      damage.source.entityId.flatMap(playerService.getPlayerOption) match {
        case Some(attacker) if attacker.team.nonEmpty =>
          gameService.awardKillingBlow(attacker.team.get)
        case _ =>
      }

    case WarFinishedEvent(war, winner) =>
    // TODO impl daily and monthly rewards when item cache is impl

    case SkirmishFinishedEvent(_, winner) =>
      playerService.getOnlinePlayers.foreach { player =>
        if (player.team.contains(winner)) {
          playerService.giveItem(player.id, winnerCrate)
        } else playerService.giveItem(player.id, loserCrate)
      }

    case _ =>
  }
}
