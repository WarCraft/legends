/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.Player

case class PlayerVoteEvent(player: Player) extends Event
