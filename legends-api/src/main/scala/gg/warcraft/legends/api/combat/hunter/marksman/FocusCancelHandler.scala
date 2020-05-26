/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter.marksman

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerDisconnectEvent

class FocusCancelHandler(repository: FocusRepository) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerDisconnectEvent(player) =>
      repository.removeFocus(player.id).foreach { _.cancel() }
    case _ =>
  }
}
