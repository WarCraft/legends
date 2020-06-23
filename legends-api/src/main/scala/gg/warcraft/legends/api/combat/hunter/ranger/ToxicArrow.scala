/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter.ranger

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}

class ToxicArrow(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService
) extends MeleeAbility {
  projectileService.createArrow(
    this,
    caster.eyeLocation,
    caster.eyeLocation.rotation
  )

  override def onHit(entity: Entity): Unit = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.addPotionEffect(entity.id, snapshot.poison)
    entityService.addPotionEffect(entity.id, snapshot.slow)
    entityService.addPotionEffect(entity.id, snapshot.nausea)
  }
}

object ToxicArrow {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new ToxicArrow(caster, snapshot)
  }
}
