package gg.warcraft.legends.menu.shop;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.shop.ShopService;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.Staff;
import gg.warcraft.monolith.ai.PetMaker;
import gg.warcraft.monolith.menu.BackButton;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonBuilder;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;
import gg.warcraft.monolith.util.RunnableUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MiniPetMenu extends Menu {
    private static final Monolith monolith = Monolith.getInstance();
    private static final Map<Player, Entity> pets = new HashMap<>();

    public MiniPetMenu(LegendsPlayer player) {
        super(player, "Mini Pets");
        ShopService shop = ShopService.getInstance();

        Button cowButton = new ButtonBuilder("MHF_Cow")
                .setTitle("Cow")
                .setAction(() -> createPet(shop.hasCalf(player.getUniqueId()) || Staff.isMod(player), player, EntityType.COW))
                .addLore(ChatColor.ITALIC + "Show bubbers!")
                .addLore("")
                .addLore(getClosingLore(shop.hasCalf(player.getUniqueId()) || Staff.isMod(player)))
                .build();

        Button chickenButton = new ButtonBuilder("MHF_Chicken")
                .setTitle("Chicken")
                .setAction(() -> createPet(shop.hasChick(player.getUniqueId()) || Staff.isMod(player), player, EntityType.CHICKEN))
                .addLore(ChatColor.ITALIC + "Why did the chicken cross the road?")
                .addLore("")
                .addLore(getClosingLore(shop.hasChick(player.getUniqueId()) || Staff.isMod(player)))
                .build();

        Button pigButton = new ButtonBuilder("MHF_Pig")
                .setTitle("Pig")
                .setAction(() -> createPet(shop.hasPiglet(player.getUniqueId()) || Staff.isMod(player), player, EntityType.PIG))
                .addLore(ChatColor.ITALIC + "Bacon is good for me!")
                .addLore("")
                .addLore(getClosingLore(shop.hasPiglet(player.getUniqueId()) || Staff.isMod(player)))
                .build();

        Button sheepButton = new ButtonBuilder("MHF_Sheep")
                .setTitle("Sheep")
                .setAction(() -> createPet(shop.hasLamb(player.getUniqueId()) || Staff.isMod(player), player, EntityType.SHEEP))
                .addLore(ChatColor.ITALIC + "A ball of wool for a fool!")
                .addLore("")
                .addLore(getClosingLore(shop.hasLamb(player.getUniqueId()) || Staff.isMod(player)))
                .build();

        update(ButtonSlot.FIRST, cowButton);
        update(ButtonSlot.SECOND, chickenButton);
        update(ButtonSlot.THIRD, pigButton);
        update(ButtonSlot.FOURTH, sheepButton);

        update(ButtonSlot.EIGHT, new Button(Material.BARRIER, "Dismiss pet", () -> {
            clearPet(player);
            RunnableUtils.runOnNextTick(player::closeInventory);
        }));
        update(ButtonSlot.NINTH, new BackButton(player, this));
    }

    private String getClosingLore(boolean petUnlocked) {
        if (petUnlocked) {
            return ChatColor.GREEN + "[CLICK] to summon me!";
        } else {
            return ChatColor.GOLD + "You can unlock me at shop.warcraft.gg!";
        }
    }

    private void clearPet(LegendsPlayer player) {
        Entity pet = pets.remove(player);
        if (pet != null) {
            pet.remove();
        }
    }

    private void createPet(boolean petUnlocked, LegendsPlayer player, EntityType type) {
        if (!petUnlocked) {
            player.sendMessage(ChatColor.GOLD + "You can unlock me at shop.warcraft.gg!");
            return;
        }
        clearPet(player);
        Entity entity = monolith.getWorld().spawnEntity(player.getLocation(), type);
        Ageable ageable = (Ageable) entity;
        LivingEntity livingEntity = (LivingEntity) entity;
        ageable.setBaby();
        PetMaker.makePet(livingEntity, player);
        pets.put(player, entity);
        RunnableUtils.runOnNextTick(player::closeInventory);
    }
}
