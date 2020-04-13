package gg.warcraft.legends.api.combat.hunter.ranger

import gg.warcraft.combat.api.ability.MeleeAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.entity.{Entity, EntityService}

object ToxicArrow {
  class Factory(
      implicit entityService: EntityService,
      projectileService: ProjectileService
  ) extends MeleeAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        target: Option[Entity]
    ): MeleeAbility = new ToxicArrow(caster, snapshot)
  }
}

class ToxicArrow(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
)(
    implicit entityService: EntityService,
    projectileService: ProjectileService
) extends MeleeAbility {
  // suggested speed of 0.6 by the spigot docs is way too low
  private final val velocity = 3
  // suggested spread of 12 by the spigot docs is way too wide
  private final val spread = 2

  projectileService createArrow (this, caster.eyeLocation, caster.eyeLocation.rotation, velocity, spread, () => {})

  override def onHit(entity: Entity): Unit = {
    entityService damageEntity (entity.id, snapshot.damage)
    entityService addPotionEffect (entity.id, snapshot.poison)
  }
}
