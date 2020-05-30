package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.combat.api.ability.AbilityPreCastEvent
import gg.warcraft.monolith.api.core.event.{Event, PreEvent}
import gg.warcraft.monolith.api.player.hiding.PlayerHidingService

class SneakCancelHandler(implicit
    playerHidingService: PlayerHidingService,
    repository: SneakRepository
) extends Event.Handler {
  override def handle(event: PreEvent): Unit = event match {
    case AbilityPreCastEvent(ability, caster, _, _, _) =>
      if (ability != "Focus") repository.remove(caster.id).foreach { it =>
        playerHidingService.reveal(it.caster.id)
      }
    case _ =>
  }
}
