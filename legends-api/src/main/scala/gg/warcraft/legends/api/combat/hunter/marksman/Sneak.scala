package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.player.hiding.PlayerHidingService
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

object Sneak {
  class Factory(
      implicit taskService: TaskService,
      entityService: EntityService,
      playerHidingService: PlayerHidingService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new Sneak(caster, snapshot)
  }
}

class Sneak(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit taskService: TaskService,
    entityService: EntityService,
    playerHidingService: PlayerHidingService,
    worldService: WorldService
) extends MeleeAbility {
  private final val soundCategory = SoundCategory.HOSTILE
  private final val castSound = Sound.ENTITY_PLAYER_BREATH

  entityService addPotionEffect (caster.id, snapshot.speed)
  playerHidingService hide caster.id
  taskService runLater (snapshot.speed.duration, () => {
    playerHidingService reveal caster.id
  })

  worldService playSound (caster.location, castSound, soundCategory)
}
