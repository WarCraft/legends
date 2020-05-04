package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}

object PowerShot {
  class Factory(implicit
      entityService: EntityService,
      projectileService: ProjectileService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new PowerShot(caster, snapshot)
  }
}

class PowerShot(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(implicit
    entityService: EntityService,
    projectileService: ProjectileService
) extends MeleeAbility {
  private val location = caster.eyeLocation
  private val direction = location.rotation

  projectileService.createArrow(this, location, direction)

  override def onHit(entity: Entity): Unit = {
    entityService.damageEntity(entity.id, snapshot.damage)
    entityService.knockBackEntity(entity.id, direction, snapshot.knockBack.modified)
  }
}
