/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat

import gg.warcraft.combat.api.ability.AbilityService
import gg.warcraft.combat.api.projectile.ProjectileService
import gg.warcraft.legends.api.combat.hunter.marksman.{
  Focus, FocusRepository, PowerShot, Sneak, SneakRepository
}
import gg.warcraft.legends.api.combat.hunter.ranger.{Disengage, ToxicArrow, Trap}
import gg.warcraft.legends.api.combat.mage.elementalist.{
  Bolt, Fireball, HailBlast, MoltenStorm
}
import gg.warcraft.legends.api.combat.mage.racial.{ArcaneBarrier, FlameTotem}
import gg.warcraft.legends.api.combat.mage.wizard.{
  ArcaneFizzle, ArcaneMissile, ArcaneShield, Juke, MagicScatter,
  MagicScatterProjectile, MassBamboozle, Teleport
}
import gg.warcraft.legends.api.combat.warrior.berserker.{Grudge, Maul, Shatter}
import gg.warcraft.legends.api.combat.warrior.guardian.{
  Hamstring, Slam, StandYourGround
}
import gg.warcraft.legends.api.combat.warrior.racial.LastStand
import gg.warcraft.monolith.api.block.spoofing.BlockSpoofingService
import gg.warcraft.monolith.api.core.task.TaskService
import gg.warcraft.monolith.api.effect.ParticleAdapter
import gg.warcraft.monolith.api.entity.EntityService
import gg.warcraft.monolith.api.entity.attribute.AttributeService
import gg.warcraft.monolith.api.entity.status.StatusService
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.player.hiding.PlayerHidingService
import gg.warcraft.monolith.api.world.WorldService

class CombatConfig(implicit
    abilityService: AbilityService,
    entityService: EntityService,
    projectileService: ProjectileService,
    worldService: WorldService,
    particleAdapter: ParticleAdapter,
    taskService: TaskService,
    statusService: StatusService,
    attributeService: AttributeService,
    playerHidingService: PlayerHidingService,
    playerService: PlayerService,
    focusRepository: FocusRepository,
    sneakRepository: SneakRepository,
    blockSpoofingService: BlockSpoofingService,
    massBamboozleRepository: MassBamboozle.Repository,
    massBamboozleAdapter: MassBamboozle.Adapter
) {
  import abilityService.registerAbility

  def writeConfig(): Unit = {
    registerMageAbilities()
    registerRangerAbilities()
    registerWarriorAbilities()
  }

  private def registerMageAbilities(): Unit = {
    // Elementalist
    registerAbility("Fireball", new Fireball.Factory())
    registerAbility("Bolt", new Bolt.Factory())
    registerAbility("Hail Blast", new HailBlast.Factory())
    registerAbility("Molten Storm", new MoltenStorm.Factory())
    // Wizard
    registerAbility("Arcane Fizzle", new ArcaneFizzle.Factory())
    registerAbility("Arcane Missile", new ArcaneMissile.Factory())
    registerAbility("Teleport", new Teleport.Factory())
    registerAbility("Mass Bamboozle", new MassBamboozle.Factory())
    // Wizard Alternate
    registerAbility("Magic Scatter", new MagicScatter.Factory())
    registerAbility(
      "Magic Scatter (Projectile)",
      new MagicScatterProjectile.Factory()
    )
    registerAbility("Arcane Shield", new ArcaneShield.Factory())
    registerAbility("Juke", new Juke.Factory())
    // Human
    registerAbility("Barrier", new ArcaneBarrier.Factory())
    // Orc
    registerAbility("Flame Totem", new FlameTotem.Factory())
  }

  private def registerRangerAbilities(): Unit = {
    // Hunter
    registerAbility("Disengage", new Disengage.Factory())
    registerAbility("Toxic Arrow", new ToxicArrow.Factory())
    registerAbility("Trap", new Trap.Factory())
    // Marksman
    registerAbility("Focus", new Focus.Factory())
    registerAbility("Power Shot", new PowerShot.Factory())
    registerAbility("Sneak", new Sneak.Factory())
  }

  private def registerWarriorAbilities(): Unit = {
    // Berserker
    registerAbility("Grudge", new Grudge.Factory())
    registerAbility("Maul", new Maul.Factory())
    registerAbility("Shatter", new Shatter.Factory())
    // Guardian
    registerAbility("Hamstring", new Hamstring.Factory())
    registerAbility("Slam", new Slam.Factory())
    registerAbility("Stand Your Ground", new StandYourGround.Factory())
    // Human
    registerAbility("Last Stand", new LastStand.Factory())
  }
}
