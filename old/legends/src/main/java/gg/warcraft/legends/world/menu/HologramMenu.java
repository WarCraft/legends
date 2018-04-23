package gg.warcraft.legends.world.menu;

import gg.warcraft.monolith.chat.ChatService;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class HologramMenu extends Menu {
    private static final double LINE_HEIGHT = 0.33;

    private final Player player;
    private final LinkedList<ArmorStand> armorStands;
    private final Location base;

    public HologramMenu(Player player, Block target) {
        super(player, "Hologram", 36);
        this.player = player;
        armorStands = new LinkedList<>();
        target.getWorld().getNearbyEntities(target.getLocation().add(0.5, 5, 0.5), 1, 10, 1).forEach(entity -> {
            if (entity.getType() == EntityType.ARMOR_STAND) {
                ArmorStand armorStand = (ArmorStand) entity;
                if (isHologramStand(armorStand)) {
                    armorStands.add(armorStand);
                }
            }
        });
        if (armorStands.isEmpty()) {
            base = target.getLocation().add(0.5, 1, 0.5);
        } else {
            base = armorStands.stream().reduce((thisOne, thatOne) -> {
                Location thisLocation = thisOne.getLocation();
                Location thatLocation = thatOne.getLocation();
                if (thisLocation.getY() <= thatLocation.getY()) {
                    return thisOne;
                } else {
                    return thatOne;
                }
            }).orElse(armorStands.get(0)).getLocation();
        }

        armorStands.sort((thisOne, thatOne) -> Double.compare(thatOne.getLocation().getY(), thisOne.getLocation().getY()));

        RunnableUtils.runOnNextTick(this::refreshView);
    }

    private boolean isHologramStand(ArmorStand armorStand) {
        return armorStand.isSmall() &&
                armorStand.isInvulnerable() &&
                !armorStand.isVisible() &&
                // NOTE no point in setting, refreshes on server restart: !armorStand.isCollidable() &&
                !armorStand.hasGravity();
    }

    private void addExtraArmorStand() {
        double yOffset = armorStands.size() * LINE_HEIGHT;
        ArmorStand armorStand = (ArmorStand) base.getWorld().spawnEntity(base.clone().add(0, yOffset, 0), EntityType.ARMOR_STAND);
        armorStand.setSmall(true);
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        // NOTE no point in setting, refreshes on server restart: armorStand.setCollidable(false);
        armorStand.setGravity(false);
        armorStands.addFirst(armorStand);

        ArmorStand previous = armorStands.get(0);
        for (int i = 1; i < armorStands.size(); i += 1) {
            ArmorStand current = armorStands.get(i);
            if (current.isCustomNameVisible()) {
                previous.setCustomNameVisible(true);
                previous.setCustomName(current.getCustomName());
            } else {
                previous.setCustomNameVisible(false);
                previous.setCustomName(ChatColor.GRAY + "_");
            }
            previous = current;
        }
        armorStands.getLast().setCustomNameVisible(false);
        armorStands.getLast().setCustomName(ChatColor.GRAY + "_");
    }

    private void refreshView() {
        int lineSlot = 0;
        for (ArmorStand armorStand : armorStands) {
            String line = armorStand.getCustomName() != null ? armorStand.getCustomName() : ChatColor.GRAY + "_";
            Button lineButton = new ButtonBuilder(Material.BOOK)
                    .setTitle(line)
                    .setAction(() -> {
                        ChatService.getInstance().registerChatListener(player, (player, message) -> {
                            if (!message.equals("_")) {
                                String colorCodedMessage = ChatColor.translateAlternateColorCodes('&', message);
                                armorStand.setCustomName(colorCodedMessage);
                                armorStand.setCustomNameVisible(true);
                            } else {
                                armorStand.setCustomName(ChatColor.GRAY + "_");
                                armorStand.setCustomNameVisible(false);
                            }

                            RunnableUtils.runOnNextTick(() -> {
                                refreshView();
                                player.openInventory(this);
                            });
                        });
                        player.sendMessage(ChatColor.YELLOW + "Type the hologram line (use _ for empty):");
                        RunnableUtils.runOnNextTick(player::closeInventory);
                    })
                    .addLore("")
                    .addLore("[CLICK] Update this line's text (use _ for empty)")
                    .build();
            update(lineSlot, lineButton);

            lineSlot += 1;
        }

        if (lineSlot < 25) {
            Button newLine = new ButtonBuilder(Material.BOOK_AND_QUILL)
                    .setTitle("Add new line")
                    .setAction(() -> {
                        addExtraArmorStand();
                        RunnableUtils.runOnNextTick(this::refreshView);
                    })
                    .addLore("")
                    .addLore("[CLICK] Add a new line to this hologram")
                    .build();
            update(lineSlot, newLine);
        }

        Button north = new ButtonBuilder(Material.COMPASS)
                .setTitle("Move North")
                .setAction(() -> armorStands.forEach(armorStand -> {
                    Location newLocation = armorStand.getLocation();
                    newLocation.add(0, 0, -1);
                    armorStand.teleport(newLocation);
                }))
                .addLore("")
                .addLore("[CLICK] Move the entire hologram one block north")
                .build();
        update(27, north);

        Button east = new ButtonBuilder(Material.COMPASS)
                .setTitle("Move East")
                .setAction(() -> armorStands.forEach(armorStand -> {
                    Location newLocation = armorStand.getLocation();
                    newLocation.add(1, 0, 0);
                    armorStand.teleport(newLocation);
                }))
                .addLore("")
                .addLore("[CLICK] Move the entire hologram one block east")
                .build();
        update(28, east);

        Button south = new ButtonBuilder(Material.COMPASS)
                .setTitle("Move South")
                .setAction(() -> armorStands.forEach(armorStand -> {
                    Location newLocation = armorStand.getLocation();
                    newLocation.add(0, 0, 1);
                    armorStand.teleport(newLocation);
                }))
                .addLore("")
                .addLore("[CLICK] Move the entire hologram one block south")
                .build();
        update(29, south);

        Button west = new ButtonBuilder(Material.COMPASS)
                .setTitle("Move West")
                .setAction(() -> armorStands.forEach(armorStand -> {
                    Location newLocation = armorStand.getLocation();
                    newLocation.add(-1, 0, 0);
                    armorStand.teleport(newLocation);
                }))
                .addLore("")
                .addLore("[CLICK] Move the entire hologram one block west")
                .build();
        update(30, west);

        Button up = new ButtonBuilder(Material.ARROW)
                .setTitle("Move Up")
                .setAction(() -> armorStands.forEach(armorStand -> {
                    Location newLocation = armorStand.getLocation();
                    newLocation.add(0, 1, 0);
                    armorStand.teleport(newLocation);
                }))
                .addLore("")
                .addLore("[CLICK] Move the entire hologram one block up")
                .build();
        update(31, up);

        Button down = new ButtonBuilder(Material.ARROW)
                .setTitle("Move Down")
                .setAction(() -> armorStands.forEach(armorStand -> {
                    Location newLocation = armorStand.getLocation();
                    newLocation.add(0, -1, 0);
                    armorStand.teleport(newLocation);
                }))
                .addLore("")
                .addLore("[CLICK] Move the entire hologram one block down")
                .build();
        update(32, down);

        Button delete = new ButtonBuilder(Material.BARRIER)
                .setTitle("Delete Hologram")
                .setAction(() -> {
                    deleteHologram();
                    RunnableUtils.runOnNextTick(player::closeInventory);
                })
                .addLore("")
                .addLore("[CLICK] Delete this hologram in its entirety")
                .build();
        update(35, delete);

        player.updateInventory();
    }

    private void deleteHologram() {
        armorStands.forEach(ArmorStand::remove);
        armorStands.clear();

        RunnableUtils.runOnNextTick(this::refreshView);
    }
}
