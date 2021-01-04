/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.currency

import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.monolith.api.core.event.{Event, EventService}
import gg.warcraft.monolith.api.player.{
  PlayerCurrencyGainedEvent, PlayerCurrencyLostEvent
}

class LegendsCurrencyEventMapper(implicit
    eventService: EventService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerCurrencyGainedEvent(player, currency, amount, current, total) =>
      val event = currency match {
        case Favor     => PlayerFavorGainedEvent(player, amount, current, total)
        case Fragments => PlayerFragmentsGainedEvent(player, amount, current, total)
        case Gold      => PlayerGoldGainedEvent(player, amount, current, total)
      }
      eventService.publish(event)

    case PlayerCurrencyLostEvent(player, currency, amount, newCurrent) =>
      val event = currency match {
        case Favor     => PlayerFavorLostEvent(player, amount, newCurrent)
        case Fragments => PlayerFragmentsLostEvent(player, amount, newCurrent)
        case Gold      => PlayerGoldLostEvent(player, amount, newCurrent)
      }
      eventService.publish(event)

    case _ =>
  }
}
