package gg.warcraft.legends.api.player.challenge

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.Player

case class PlayerChallengeCompletedEvent(
    player: Player,
    challenge: Challenge
) extends Event
