/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.gathering.gatherable.BlockPreGatherEvent
import gg.warcraft.housing.api.area.HousingAreaService
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.siege.api.objective.ObjectiveService
import gg.warcraft.siege.api.weapon.SiegeWeaponService

class GatheringBlacklistHandler(implicit
    objectiveService: ObjectiveService,
    siegeWeaponService: SiegeWeaponService,
    housingAreaService: HousingAreaService
) extends Event.Handler {
  override def reduce[T <: PreEvent](event: T): T = event match {
    case it @ BlockPreGatherEvent(block, spot, player, _, _) =>
      if (
        objectiveService.objectives.values.exists { _.blocks.contains(block) }
        || siegeWeaponService.weapons.contains(block.location)
        || housingAreaService.areas.exists { _.boundingBox.test(block.location) }
      ) {
        it.copy(cancelled = true).asInstanceOf[T]
      } else event
    case _ => event
  }
}
