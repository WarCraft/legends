package gg.warcraft.legends.spigot.core.handler;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnePointEightPvPInitializationHandler implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setMaximumNoDamageTicks(17);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(
                new AttributeModifier("generic.attackSpeed", 9.9999999E7D, AttributeModifier.Operation.ADD_NUMBER));
    }

    private void onPlayerDisconnectEvent(Player player) {
        for (AttributeModifier modifier : player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers()) {
            if (modifier.getName().equals("generic.attackSpeed")) {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(modifier);
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        onPlayerDisconnectEvent(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerKickEvent event) {
        onPlayerDisconnectEvent(event.getPlayer());
    }
}
