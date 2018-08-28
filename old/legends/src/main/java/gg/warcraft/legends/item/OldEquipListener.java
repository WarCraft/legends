package gg.warcraft.legends.item;

import org.bukkit.event.Listener;

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
