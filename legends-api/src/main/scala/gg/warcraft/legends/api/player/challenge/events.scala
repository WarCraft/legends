/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.Player

case class ChallengeCompleteEvent(
    player: Player,
    challenge: Challenge
) extends Event
