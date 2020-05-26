/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.currency

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.Player

// FAVOR
case class PlayerFavorGainedEvent(
    player: Player,
    amount: Int,
    newCurrent: Int,
    newLifeTime: Int
) extends Event

case class PlayerFavorLostEvent(
    player: Player,
    amount: Int,
    newCurrent: Int
) extends Event

// FRAGMENTS
case class PlayerFragmentsGainedEvent(
    player: Player,
    amount: Int,
    newCurrent: Int,
    newLifeTime: Int
) extends Event

case class PlayerFragmentsLostEvent(
    player: Player,
    amount: Int,
    newCurrent: Int
) extends Event

// GOLD
case class PlayerGoldGainedEvent(
    player: Player,
    amount: Int,
    newCurrent: Int,
    newLifeTime: Int
) extends Event

case class PlayerGoldLostEvent(
    player: Player,
    amount: Int,
    newCurrent: Int
) extends Event
