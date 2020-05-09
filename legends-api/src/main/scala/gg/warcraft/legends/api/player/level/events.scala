package gg.warcraft.legends.api.player.level

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.Player

case class PlayerLevelUpEvent(
    player: Player,
    level: Level
) extends Event
