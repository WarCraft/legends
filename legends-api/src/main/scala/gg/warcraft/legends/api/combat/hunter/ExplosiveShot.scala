package gg.warcraft.legends.api.combat.hunter

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Location, WorldService}

object ExplosiveShot {
  class Factory(
      implicit entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new ExplosiveShot(caster, snapshot)
  }
}

class ExplosiveShot(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends MeleeAbility {
  // suggested speed of 0.6 by the spigot docs is way too low
  private final val velocity = 3
  // suggested spread of 12 by the spigot docs is way too wide
  private final val spread = 2

  private val arrowId = projectileService createArrow (this, caster.eyeLocation, caster.eyeLocation.rotation, velocity, spread, () => {})

  private def explode(location: Location): Unit = {
    entityService.getNearbyEntities(location, snapshot.radius.modified) foreach {
      it =>
        entityService damageEntity (it.id, snapshot.damage)
        entityService knockBackEntity (it.id, snapshot.knockBack.modified)
    }
    worldService createExplosion (location, true)
  }

  override def onHit(entity: Entity): Unit =
    explode(entity.location)

  // TODO explode arrow onGrounded
}
