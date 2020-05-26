/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Location, WorldService}

class ExplosiveShot(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends MeleeAbility {
  private val knockBackDirection = caster.eyeLocation.rotation
  private val arrowId = projectileService.createArrow(
    this,
    caster.eyeLocation,
    caster.eyeLocation.rotation
  )

  private def explode(location: Location): Unit = {
    entityService.getNearbyEntities(location, snapshot.radius.modified).foreach { it =>
        entityService.damageEntity(it.id, snapshot.damage)
        entityService.knockBackEntity(it.id, knockBackDirection, snapshot.knockBack.modified)
    }
    worldService.createExplosion(location, true)
  }

  override def onHit(entity: Entity): Unit =
    explode(entity.location)

  // TODO explode arrow onGrounded
}

object ExplosiveShot {
  class Factory(implicit
      entityService: EntityService,
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
