package gg.warcraft.legends.api.combat.mage.racial

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.{AbilityService, RangedAbility}
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.combat.CombatSource
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.Duration._
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.entity.{
  Entity, EntityHealthChangedEvent, EntityService
}
import gg.warcraft.monolith.api.entity.status.Status
import gg.warcraft.monolith.api.world.WorldService

object UnstableBunker {
  class Factory(
      implicit taskService: TaskService,
      entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService
  ) extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new UnstableBunker(caster, snapshot)
  }

  class StatusEffect(
      override val source: CombatSource,
      override val duration: Option[Duration]
  )(
      implicit abilityService: AbilityService
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    override val name: String = "Unstable Bunker"
    override val group: String = "legends.racial"

    override def handle(event: Event): Unit = event match {
      case EntityHealthChangedEvent(_, _, _, _, _, newPercentHealth) =>
        if (newPercentHealth <= 0.5f)
          abilityService castAbility (name, source.entityId.get, None)
      case _ =>
    }
  }
}

class UnstableBunker(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit taskService: TaskService,
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends RangedAbility {
  taskService runLater (2.seconds, () => {
    val movement = Movement.oneOff(caster.location)
    val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
    projectileService createProjectile (this, movement, hitbox, () => {}) // TODO add default param value to callback
  })

  override def onHit(entity: Entity): Boolean = {
    entityService damageEntity (entity.id, snapshot.damage)
    entityService knockBackEntity (entity.id, caster.location, snapshot.knockBack.modified)
    false
  }

  override def onMiss(entity: Entity): Boolean = false
}
