package gg.warcraft.legends.api.player.currency

import gg.warcraft.monolith.api.core.event.{Event, EventService}
import gg.warcraft.monolith.api.player.{
  PlayerCurrencyGainedEvent, PlayerCurrencyLostEvent
}

class LegendsCurrencyEventMapper(implicit
    eventService: EventService
) extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case event: PlayerCurrencyGainedEvent =>
      import event.{amount, currency, newCurrent, newLifeTime, playerId}
      eventService.publish(currency match {
        case LegendsCurrency.FAVOR.toString =>
          PlayerFavorGainedEvent(playerId, amount, newCurrent, newLifeTime)
        case LegendsCurrency.FRAGMENTS.toString =>
          PlayerFragmentsGainedEvent(playerId, amount, newCurrent, newLifeTime)
        case LegendsCurrency.GOLD.toString =>
          PlayerGoldGainedEvent(playerId, amount, newCurrent, newLifeTime)
      })

    case event: PlayerCurrencyLostEvent =>
      import event.{amount, currency, newCurrent, playerId}
      eventService.publish(currency match {
        case LegendsCurrency.FAVOR.toString =>
          PlayerFavorLostEvent(playerId, amount, newCurrent)
        case LegendsCurrency.FRAGMENTS.toString =>
          PlayerFragmentsLostEvent(playerId, amount, newCurrent)
        case LegendsCurrency.GOLD.toString =>
          PlayerGoldLostEvent(playerId, amount, newCurrent)
      })

    case _ =>
  }
}
