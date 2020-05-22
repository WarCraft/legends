package gg.warcraft.legends.spigot

import org.bukkit.attribute.{Attribute, AttributeModifier}
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.player.{PlayerJoinEvent, PlayerKickEvent, PlayerQuitEvent}

class OnePointEightPvPInitHandler extends Listener {
  private final val attackSpeedModifier = new AttributeModifier(
    "generic.attackSpeed",
    9.9999999e7d,
    AttributeModifier.Operation.ADD_NUMBER
  )

  @EventHandler
  def onPlayerJoinEvent(event: PlayerJoinEvent): Unit = {
    event.getPlayer.setMaximumNoDamageTicks(17)
    event.getPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
      .addModifier(attackSpeedModifier)
  }

  def onPlayerDisconnectEvent(player: Player): Unit = {
    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
      .getModifiers.forEach { modifier =>
        if (modifier.getName == "generic.attackSpeed") {
          player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
            .removeModifier(modifier)
        }
      }
  }

  @EventHandler
  def onPlayerQuitEvent(event: PlayerQuitEvent): Unit = {
    onPlayerDisconnectEvent(event.getPlayer)
  }

  @EventHandler
  def onPlayerKickEvent(event: PlayerKickEvent): Unit = {
    onPlayerDisconnectEvent(event.getPlayer)
  }
}
