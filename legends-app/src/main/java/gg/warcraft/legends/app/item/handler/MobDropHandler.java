package gg.warcraft.legends.app.item.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.legends.api.combat.LegendsSpecialization;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.equipment.EquipmentGenerator;
import gg.warcraft.legends.api.item.equipment.EquipmentGeneratorFactory;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.core.PluginLogger;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.event.ItemPrePickupEvent;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.WorldType;
import gg.warcraft.monolith.api.world.location.Location;
import gg.warcraft.monolith.api.world.location.LocationFactory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class MobDropHandler implements CommandHandler {
    private static final int RESERVE_DURATION = TimeUtils.MILLIS_PER_MINUTE;

    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final WorldCommandService worldCommandService;
    private final EquipmentGeneratorFactory equipmentGeneratorFactory;
    private final LocationFactory locationFactory;
    private final Random random;
    private final Logger pluginLogger;
    private final Map<UUID, UUID> reservedFor;
    private final Map<UUID, Long> reservedUntil;

    @Inject
    public MobDropHandler(LegendsPlayerQueryService playerQueryService,
                          LegendsPlayerCommandService playerCommandService,
                          WorldCommandService worldCommandService,
                          EquipmentGeneratorFactory equipmentGeneratorFactory,
                          LocationFactory locationFactory,
                          Random random, @PluginLogger Logger pluginLogger) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.worldCommandService = worldCommandService;
        this.equipmentGeneratorFactory = equipmentGeneratorFactory;
        this.locationFactory = locationFactory;
        this.random = random;
        this.pluginLogger = pluginLogger;
        this.reservedFor = new HashMap<>();
        this.reservedUntil = new HashMap<>();
    }

    private int getGoldAmount(String mobType) {
        switch (mobType) {
            case "Dunewalker":
            case "Charred":
                return random.nextInt(2);
            case "Elemental":
            case "Rider":
                return random.nextInt(3) + 3;
            default:
                return 0;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isConsole()) {
            return false;
        }

        if (arguments.size() != 5) {
            return false;
        }

        String mobType;
        UUID mobId;
        float x;
        float y;
        float z;
        try {
            mobType = arguments.get(0);
            mobId = UUID.fromString(arguments.get(1));
            x = Float.parseFloat(arguments.get(2));
            y = Float.parseFloat(arguments.get(3));
            z = Float.parseFloat(arguments.get(4));
        } catch (Exception ex) {
            System.out.println("Failed to drop items for " + arguments);
            return false;
        }

        EquipmentGenerator equipmentGenerator = equipmentGeneratorFactory.createEquipmentGenerator();
        Set<UUID> damagers = DamageService.getInstance().getDamagers(mobId);
        damagers.forEach(damagerId -> {
            LegendsPlayer damager = playerQueryService.getPlayer(damagerId);
            if (damager == null) {
                return; // damage was probably a pet or another mob
            }

            LegendsTeam team = damager.getTeam();
            Specialization specialization = damager.getSpecialization();
            LegendsSpecializationType specializationType = null;
            for (LegendsSpecialization legendsSpecialization : LegendsSpecialization.values()) {
                if (legendsSpecialization.getName().equals(specialization.getName())) {
                    specializationType = legendsSpecialization.getType();
                }
            }
            if (specializationType == null) {
                pluginLogger.warning("Failed to drop random equipment for player " + damagerId +
                        ", could not determine specialization type for " + specialization.getName());
                return false;
            }

            Item drop = equipmentGenerator.generate(specializationType, team);
            Location dropLocation = locationFactory.createLocation(WorldType.OVERWORLD, x, y, z);
            List<UUID> droppedItemIds = worldCommandService.dropItemsAt(Collections.singletonList(drop), dropLocation);
            UUID droppedItemId = droppedItemIds.get(0);

            UUID secondDroppedItemId = null;
            if (mobType.equals("Rider") || mobType.equals("Elemental")) {
                if (random.nextDouble() >= 0.5) {
                    Item secondDrop = equipmentGenerator.generate(specializationType, team);
                    List<UUID> secondDroppedItemIds = worldCommandService.dropItemsAt(Collections.singletonList(secondDrop), dropLocation);
                    secondDroppedItemId = secondDroppedItemIds.get(0);
                }
            }

            int goldDropped = getGoldAmount(mobType);
            if (goldDropped > 0) {
                playerCommandService.addGold(damagerId, goldDropped);
                playerCommandService.sendNotification(damagerId, "You looted " + goldDropped + " gold.");
            }

            reservedFor.put(droppedItemId, damagerId);
            reservedUntil.put(droppedItemId, System.currentTimeMillis() + RESERVE_DURATION);

            if (secondDroppedItemId != null) {
                reservedFor.put(secondDroppedItemId, damagerId);
                reservedUntil.put(secondDroppedItemId, System.currentTimeMillis() + RESERVE_DURATION);
            }
        });
        return true;
    }

    @Subscribe
    public void onItemPrePickupEvent(ItemPrePickupEvent event) {
        UUID itemId = event.getItemId();
        boolean stillReserved = reservedUntil.getOrDefault(itemId, 0L) > System.currentTimeMillis();
        if (stillReserved) {
            if (!reservedFor.get(itemId).equals(event.getEntityId())) {
                event.cancel();
            }
        } else {
            reservedUntil.remove(itemId);
            reservedFor.remove(itemId);
        }
    }
}
