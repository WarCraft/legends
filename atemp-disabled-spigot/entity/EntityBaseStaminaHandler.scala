package gg.warcraft.legends.spigot.entity

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerConnectEvent

class EntityBaseStaminaHandler extends Event.Handler {
  override def handle(event: Event): Unit = event match {
    case PlayerConnectEvent(player) =>
      // TODO
      /*
            AttributeInstance spigotAttributeInstance = livingEntity.getAttribute(spigotAttribute);
            double defaultValue = spigotAttributeInstance.getDefaultValue();
            spigotAttributeInstance.setBaseValue(defaultValue + monolithValue);
       */
  }
}
