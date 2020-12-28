/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

class SpigotMiniPetFactory {

}

/* TODO

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
            case CHICKEN:
                return org.bukkit.entity.EntityType.CHICKEN;
            case COW:
                return org.bukkit.entity.EntityType.COW;
            case LLAMA:
                return org.bukkit.entity.EntityType.LLAMA;
            case OCELOT:
                return org.bukkit.entity.EntityType.OCELOT;
            case PIG:
                return org.bukkit.entity.EntityType.PIG;
            case RABBIT:
                return org.bukkit.entity.EntityType.RABBIT;
            case SHEEP:
                return org.bukkit.entity.EntityType.SHEEP;
            case SILVERFISH:
                return org.bukkit.entity.EntityType.SILVERFISH;
            case WOLF:
                return org.bukkit.entity.EntityType.WOLF;
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
        playerIdToPetId.values().forEach(id -> {
            Entity entity = server.getEntity(id);
            if (entity != null) {
                entity.remove();
            }
        });
    }
}
 */