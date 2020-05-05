package gg.warcraft.legends.api.player.currency

import java.util.UUID

import gg.warcraft.monolith.api.core.event.Event

// FAVOR
case class PlayerFavorGainedEvent(
    playerId: UUID,
    amount: Int,
    newCurrent: Int,
    newLifeTime: Int
) extends Event

case class PlayerFavorLostEvent(
    playerId: UUID,
    amount: Int,
    newCurrent: Int
) extends Event

// FRAGMENTS
case class PlayerFragmentsGainedEvent(
    playerId: UUID,
    amount: Int,
    newCurrent: Int,
    newLifeTime: Int
) extends Event

case class PlayerFragmentsLostEvent(
    playerId: UUID,
    amount: Int,
    newCurrent: Int
) extends Event

// GOLD
case class PlayerGoldGainedEvent(
    playerId: UUID,
    amount: Int,
    newCurrent: Int,
    newLifeTime: Int
) extends Event

case class PlayerGoldLostEvent(
    playerId: UUID,
    amount: Int,
    newCurrent: Int
) extends Event
