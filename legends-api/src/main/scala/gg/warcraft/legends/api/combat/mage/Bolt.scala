package gg.warcraft.legends.api.combat.mage

import gg.warcraft.combat.api.{Hitbox, Movement}
import gg.warcraft.combat.api.ability.RangedAbility.Flag
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.{Entity, EntityService}
import gg.warcraft.monolith.api.world.{Location, WorldService}

object Bolt {
  class Factory(
      implicit entityService: EntityService,
      projectileService: ProjectileService,
      worldService: WorldService
  ) extends RangedAbility.Factory {
    override val flags: Set[Flag] =
      Set(RangedAbility.TARGET_REQUIRED)

    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new Bolt(caster, snapshot, blockTarget, entityTarget)
  }
}

class Bolt(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot,
    blockTarget: Option[Block],
    entityTarget: Option[Entity]
)(
    implicit entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService
) extends RangedAbility {
  private var location: Location =
    if (entityTarget.isDefined) entityTarget.get.location
    else blockTarget.get.location.add(0.5f, 1, 0.5f)

  private val movement = Movement.oneOff(location)
  private val hitbox = Hitbox.sphere(snapshot.radius.modified, _.team != caster.team)
  projectileService createProjectile (this, movement, hitbox, () => {}) // TODO set default callback

  worldService strikeLightning (location, true)

  override def onHit(entity: Entity): Boolean = {
    entityService damageEntity (entity.id, snapshot.damage)
    true
  }

  override def onMiss(entity: Entity): Boolean = true
}
