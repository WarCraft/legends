package gg.warcraft.legends.item;

import gg.warcraft.legends.entity.LegendsAttribute;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.entity.Attribute;
import gg.warcraft.monolith.entity.AttributeModifier;
import gg.warcraft.monolith.item.ItemService;
import gg.warcraft.monolith.item.ItemStackWrapper;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
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

public class OldEquipListener implements Listener {
//    private static final String METADATA_KEY = OldEquipListener.class.getCanonicalName();
//
//    private final Plugin plugin;
//    private final LegendsPlayerService playerService;
//    private final ItemService itemService;
//
//    public OldEquipListener() {
//        this(Monolith.getInstance(), LegendsPlayerService.getInstance(), ItemService.getInstance());
//    }
//
//    public OldEquipListener(Plugin plugin, LegendsPlayerService playerService, ItemService itemService) {
//        this.plugin = plugin;
//        this.playerService = playerService;
//        this.itemService = itemService;
//    }
//
//    private List<ItemStack> getEquipment(LegendsPlayer player) {
//        List<ItemStack> equipment = new ArrayList<>();
//        equipment.add(player.getInventory().getHelmet());
//        equipment.add(player.getInventory().getChestplate());
//        equipment.add(player.getInventory().getLeggings());
//        equipment.add(player.getInventory().getBoots());
//        equipment.add(player.getInventory().getItemInMainHand());
//        equipment.add(player.getInventory().getItemInOffHand());
//        return equipment.stream().filter(Objects::nonNull).collect(Collectors.toList());
//    }
//
//    @SuppressWarnings("unchecked")
//    private void setEquipmentAttributeModifiers(UUID uniqueId) {
//        LegendsPlayer player = playerService.getLegendsPlayer(uniqueId);
//        List<ItemStack> equipment = getEquipment(player);
//        Map<Attribute, Double> attributes = new HashMap<>();
//        equipment.forEach(item -> {
//            ItemStackWrapper wrapper = itemService.newWrapper(item);
//            Map<Attribute, Double> itemAttributes = wrapper.getAttributes(Arrays.asList(LegendsAttribute.values()));
//            itemAttributes.forEach((attribute, value) -> attributes.put(attribute, attributes.getOrDefault(attribute, 0D) + value));
//        });
//
//        List<MetadataValue> currentMetadata = player.getMetadata(METADATA_KEY);
//        if (!currentMetadata.isEmpty()) {
//            List<AttributeModifier> currentModifiers = (List<AttributeModifier>) currentMetadata.get(0).value();
//            currentModifiers.forEach(player::removeAttributeModifier);
//        }
//
//        List<AttributeModifier> newModifiers = new ArrayList<>();
//        attributes.forEach((attribute, value) -> {
//            AttributeModifier modifier = new AttributeModifier(attribute);
//            modifier.setValue(value);
//            newModifiers.add(modifier);
//        });
//
//        MetadataValue newMetadata = new FixedMetadataValue(plugin, newModifiers);
//        player.setMetadata(METADATA_KEY, newMetadata);
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onPlayerJoinEvent(PlayerJoinEvent event) {
//        RunnableUtils.runOnNextTick(() -> {
//            setEquipmentAttributeModifiers(event.getPlayer().getUniqueId());
//        });
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void onInventoryClickEvent(InventoryClickEvent event) {
//        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
//            RunnableUtils.runOnNextTick(() -> {
//                setEquipmentAttributeModifiers(event.getWhoClicked().getUniqueId());
//            });
//        }
//    }
//
//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
//        RunnableUtils.runOnNextTick(() -> {
//            setEquipmentAttributeModifiers(event.getPlayer().getUniqueId());
//        });
//    }
}
