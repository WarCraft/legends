/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.core.Cancellable
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.player.hiding.PlayerHidingService
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class Sneak(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    taskService: TaskService,
    entityService: EntityService,
    playerHidingService: PlayerHidingService,
    worldService: WorldService,
    sneakRepository: SneakRepository
) extends MeleeAbility
    with Cancellable {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_PLAYER_BREATH

  entityService.addPotionEffect(caster.id, snapshot.speed)
  sneakRepository.add(this)
  playerHidingService.hide(caster.id)
  taskService.runLater(snapshot.duration, cancel)

  worldService.playSound(caster.location, castSound, soundCategory)

  override def cancel(): Unit =
    sneakRepository.remove(caster.id).foreach { it =>
      playerHidingService.reveal(it.caster.id)
    }
}

object Sneak {
  class Factory(implicit
      taskService: TaskService,
      entityService: EntityService,
      playerHidingService: PlayerHidingService,
      worldService: WorldService,
      sneakRepository: SneakRepository
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Sneak(caster, snapshot)
  }
}
