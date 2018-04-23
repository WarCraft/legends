package gg.warcraft.legends.player;

import com.mojang.authlib.GameProfile;
import gg.warcraft.monolith.event.MonolithPlayerConnectEvent;
import gg.warcraft.monolith.player.PlayerDisplayNameChangedEvent;
import gg.warcraft.monolith.player.PlayerService;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class PlayerDisplayNameTracker implements Listener {

    private final PlayerService playerService;

    public PlayerDisplayNameTracker(PlayerService playerService) {
        this.playerService = playerService;
    }

    private void updateDisplayNameForPlayer(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        // remove old player data packet
        PacketPlayOutPlayerInfo removePlayerPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        playerService.getOnlinePlayers().stream()
                .map(onlinePlayer -> ((CraftPlayer) player).getHandle())
                .forEach(craftPlayer -> craftPlayer.playerConnection.sendPacket(removePlayerPacket));

        // update game profile data
        try {
            GameProfile profile = entityPlayer.getProfile();
            Field nameField = profile.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(profile, player.getDisplayName());
        } catch (Exception ex) {
            System.out.println("Failed to update name on player game profile");
            ex.printStackTrace();
        }

        // update new player data packet
        PacketPlayOutPlayerInfo addPlayerPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        playerService.getOnlinePlayers().stream()
                .map(onlinePlayer -> ((CraftPlayer) player).getHandle())
                .forEach(craftPlayer -> craftPlayer.playerConnection.sendPacket(addPlayerPacket));


        // destroy current entity for other players
        PacketPlayOutEntityDestroy destroyEntityPacket = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        playerService.getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer != player)
                .map(onlinePlayer -> ((CraftPlayer) player).getHandle())
                .forEach(craftPlayer -> craftPlayer.playerConnection.sendPacket(destroyEntityPacket));

        // respawn new entity for other players
        PacketPlayOutNamedEntitySpawn spawnEntityPacket = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        playerService.getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer != player)
                .map(onlinePlayer -> ((CraftPlayer) player).getHandle())
                .forEach(craftPlayer -> craftPlayer.playerConnection.sendPacket(spawnEntityPacket));
    }

    @EventHandler
    public void onPlayerConnect(MonolithPlayerConnectEvent event) {
        updateDisplayNameForPlayer(event.getPlayer().getPlayer());
    }

    @EventHandler
    public void onPlayerDisplayNameChangedEvent(PlayerDisplayNameChangedEvent event) {
        updateDisplayNameForPlayer(event.getPlayer());
    }
}
