package gg.warcraft.legends.api.item.crate

import java.util.UUID

import gg.warcraft.monolith.api.block.BlockPreInteractEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.entity.EntityPreInteractEvent
import gg.warcraft.monolith.api.item.Item

class LootCrateUseHandler(lootCrateService: LootCrateService) extends Event.Handler {
  private def onInteract(playerId: UUID, item: Option[Item]): Boolean = item match {
    case Some(item) =>
      lootCrateService.crates
        .find { case (_, crate) => item == crate }
        .map { _._2 }
        .exists { crate => lootCrateService.openCrate(playerId, crate.id) }
    case None => false
  }

  override def reduce[T <: PreEvent](event: T): T = event match {
    case event @ BlockPreInteractEvent(_, _, playerId, _, mainHand, _, _, _) =>
      if (onInteract(playerId, mainHand))
        event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
    case event @ EntityPreInteractEvent(_, _, playerId, _, mainHand, _, _, _, _) =>
      if (onInteract(playerId, mainHand))
        event.copy(cancelled = true).asInstanceOf[T]
      else event.asInstanceOf[T]
  }
}
