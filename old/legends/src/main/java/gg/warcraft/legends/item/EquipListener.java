package gg.warcraft.legends.item;

import gg.warcraft.legends.entity.LegendsAttribute;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.entity.Attribute;
import gg.warcraft.monolith.entity.AttributeModifier;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class EquipListener implements Listener {
    private static final String METADATA_KEY = EquipListener.class.getCanonicalName();

    private final Plugin plugin;
    private final LegendsPlayerService playerService;

    public EquipListener() {
        this(Monolith.getInstance(), LegendsPlayerService.getInstance());
    }

    public EquipListener(Plugin plugin, LegendsPlayerService playerService) {
        this.plugin = plugin;
        this.playerService = playerService;
    }

    private List<ItemStack> getEquipment(LegendsPlayer player) {
        List<ItemStack> equipment = new ArrayList<>();
        if (player.getSpecialization() == null) {
            return equipment;
        }
        equipment.add(player.getInventory().getHelmet());
        equipment.add(player.getInventory().getChestplate());
        equipment.add(player.getInventory().getLeggings());
        equipment.add(player.getInventory().getBoots());

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (player.getSpecialization().isValidMainHandWeapon(mainHand.getType())) {
            equipment.add(mainHand);
        }
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (player.getSpecialization().isValidOffHandWeapon(offHand.getType())) {
            equipment.add(offHand);
        }

        return equipment.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void setEquipmentAttributeModifiers(UUID uniqueId) {
        LegendsPlayer player = playerService.getLegendsPlayer(uniqueId);
        List<ItemStack> equipment = getEquipment(player);
        Map<Attribute, Integer> attributes = new HashMap<>();
        equipment.forEach(item -> {
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return;
            }
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.forEach(line -> {
                Arrays.stream(LegendsAttribute.values()).forEach(attribute -> {
                    if (line.contains(attribute.getName())) {
                        int valueStart = line.indexOf("+");
                        int value = Integer.parseInt(line.substring(valueStart + 1));
                        attributes.put(attribute, attributes.getOrDefault(attribute, 0) + value);
                    }
                });
            });
        });

        List<MetadataValue> currentMetadata = player.getMetadata(METADATA_KEY);
        if (!currentMetadata.isEmpty()) {
            List<AttributeModifier> currentModifiers = (List<AttributeModifier>) currentMetadata.get(0).value();
            currentModifiers.forEach(player::removeAttributeModifier);
        }

        List<AttributeModifier> newModifiers = new ArrayList<>();
        attributes.forEach((attribute, value) -> {
            AttributeModifier modifier = new AttributeModifier(attribute);
            modifier.setValue(value);
            newModifiers.add(modifier);
        });

        newModifiers.forEach(player::addAttributeModifier);
        MetadataValue newMetadata = new FixedMetadataValue(plugin, newModifiers);
        player.setMetadata(METADATA_KEY, newMetadata);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        RunnableUtils.runOnNextTick(() -> {
            setEquipmentAttributeModifiers(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.ARMOR ||
                event.getSlotType() == InventoryType.SlotType.QUICKBAR ||
                event.getRawSlot() == 45 || // offhand slot
                event.isShiftClick()) {
            RunnableUtils.runOnNextTick(() -> {
                setEquipmentAttributeModifiers(event.getWhoClicked().getUniqueId());
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        RunnableUtils.runOnNextTick(() -> {
            setEquipmentAttributeModifiers(event.getPlayer().getUniqueId());
        });
    }
}
