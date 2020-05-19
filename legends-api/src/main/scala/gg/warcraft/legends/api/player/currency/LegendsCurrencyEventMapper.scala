package gg.warcraft.legends.api.player.currency

import gg.warcraft.legends.api.player.currency.LegendsCurrency.{
  Favor, Fragments, Gold
}
import gg.warcraft.monolith.api.core.event.{Event, EventService}
import gg.warcraft.monolith.api.player.{
  PlayerCurrencyGainedEvent, PlayerCurrencyLostEvent
}

class LegendsCurrencyEventMapper(implicit
    eventService: EventService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerCurrencyGainedEvent(player, currency, amount, current, lifetime) =>
      val event = currency match {
        case Favor.name     => PlayerFavorGainedEvent(player, amount, current, lifetime)
        case Fragments.name => PlayerFragmentsGainedEvent(player, amount, current, lifetime)
        case Gold.name      => PlayerGoldGainedEvent(player, amount, current, lifetime)
      }
      eventService.publish(event)

    case PlayerCurrencyLostEvent(player, currency, amount, newCurrent) =>
      val event = currency match {
        case Favor.name     => PlayerFavorLostEvent(player, amount, newCurrent)
        case Fragments.name => PlayerFragmentsLostEvent(player, amount, newCurrent)
        case Gold.name      => PlayerGoldLostEvent(player, amount, newCurrent)
      }
      eventService.publish(event)

    case _ =>
  }
}
