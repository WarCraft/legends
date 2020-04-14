package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerDisconnectEvent

class FocusCancelHandler(repository: FocusRepository) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerDisconnectEvent(playerId, _) =>
      repository removeFocus playerId match {
        case Some(focus) => focus.cancel()
        case None        =>
      }
    case _ =>
  }
}
