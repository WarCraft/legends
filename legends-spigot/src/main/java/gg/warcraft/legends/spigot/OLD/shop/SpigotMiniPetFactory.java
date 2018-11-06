package gg.warcraft.legends.spigot.OLD.shop;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.MiniPetFactory;
import gg.warcraft.monolith.api.core.event.ServerShutdownEvent;
import gg.warcraft.monolith.api.entity.EntityType;
import org.bukkit.Server;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpigotMiniPetFactory implements MiniPetFactory {
    private static final Map<UUID, UUID> playerIdToPetId = new HashMap<>();

    private final Server server;
    private final gg.warcraft.legends.api.ai.MiniPetFactory miniPetFactory;

    @Inject
    public SpigotMiniPetFactory(Server server, gg.warcraft.legends.api.ai.MiniPetFactory miniPetFactory) {
        this.server = server;
        this.miniPetFactory = miniPetFactory;
    }

    org.bukkit.entity.EntityType mapEntityType(EntityType type) {
        switch (type) {
            case COW:
                return org.bukkit.entity.EntityType.COW;
            case CHICKEN:
                return org.bukkit.entity.EntityType.CHICKEN;
            case PIG:
                return org.bukkit.entity.EntityType.PIG;
            case SHEEP:
                return org.bukkit.entity.EntityType.SHEEP;
            default:
                throw new IllegalArgumentException("Mini pet type " + type + " isn't supported yet in MiniPetFactory hack");
        }
    }

    @Override
    public void removePet(UUID playerId) {
        UUID petId = playerIdToPetId.remove(playerId);
        if (petId != null) {
            server.getEntity(petId).remove();
        }
    }

    @Override
    public void spawnPet(EntityType petType, UUID playerId) {
        removePet(playerId);
        Player player = server.getPlayer(playerId);
        org.bukkit.entity.EntityType spigotEntityType = mapEntityType(petType);
        Entity pet = player.getLocation().getWorld().spawnEntity(player.getLocation(), spigotEntityType);
        Ageable ageable = (Ageable) pet;
        LivingEntity livingEntity = (LivingEntity) pet;
        ageable.setBaby();
        miniPetFactory.create(livingEntity.getUniqueId(), player.getUniqueId());
        playerIdToPetId.put(playerId, pet.getUniqueId());
    }

    @Subscribe
    public void onServerShutdownEvent(ServerShutdownEvent event) {
        playerIdToPetId.values().forEach(id -> server.getEntity(id).remove());
    }
}
