/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter

import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.{AbilityService, MeleeAbility}
import gg.warcraft.monolith.api.core.Duration.DurationOps
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.Entity
import gg.warcraft.monolith.api.player.Player

class PetRespawns(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    taskService: TaskService,
    abilityService: AbilityService
) extends MeleeAbility {
  taskService.runLater(snapshot.cooldown, () => respawn())

  private def respawn(): Unit =
    caster match {
      case player: Player if player.isOnline =>
        if (player.isAlive) {
          abilityService.spawnChild("Respawn", this, Map.empty)
        } else taskService.runLater(1.seconds, () => respawn())
      case _ =>
    }
}

object PetRespawns {
  class Factory(implicit
      taskService: TaskService,
      abilityService: AbilityService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new PetRespawns(caster, snapshot)
  }
}
