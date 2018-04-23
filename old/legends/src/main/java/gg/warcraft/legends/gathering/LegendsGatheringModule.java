package gg.warcraft.legends.gathering;

import de.slikey.effectlib.util.RandomUtils;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.gathering.BlockGatherable;
import gg.warcraft.monolith.gathering.BlockGatherableFactory;
import gg.warcraft.monolith.gathering.EntityGatherable;
import gg.warcraft.monolith.gathering.GatheringService;
import gg.warcraft.monolith.gathering.SimpleBlockGatherable;
import gg.warcraft.monolith.gathering.SimpleBlockGatherableFactory;
import gg.warcraft.monolith.gathering.SimpleBlockGatheringSpot;
import gg.warcraft.monolith.gathering.SimpleEntityGatherable;
import gg.warcraft.monolith.gathering.SimpleEntityGatheringSpot;
import gg.warcraft.monolith.util.BlockOffsets;
import gg.warcraft.monolith.util.BoundingBlockBox;
import gg.warcraft.monolith.util.Duration;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class LegendsGatheringModule {

    public LegendsGatheringModule(Monolith monolith, GatheringService gatheringService) {
        World world = monolith.getWorld();

        Duration defaultCooldown = new Duration(5, Duration.Type.SECONDS);
        Supplier<Duration> defaultCooldownSupplier = () -> defaultCooldown;

        BlockGatherableFactory blockGatherableFactory = new SimpleBlockGatherableFactory();

        // Human lumber mill
        {
            Location location = new Location(world, -780, 33, 397);
            BlockOffsets offsets = new BlockOffsets(0, 20, 24, 0, 1, 0);
            BoundingBlockBox area = new BoundingBlockBox(offsets, location, BlockFace.NORTH);

            Set<Material> materials = new HashSet<>();
            materials.add(Material.LOG);
            materials.add(Material.LOG_2);

            BlockGatherable gatherable = blockGatherableFactory.create(
                    materials,
                    Material.BEDROCK,
                    () -> Collections.singletonList(getWoodDrop()),
                    defaultCooldownSupplier);
            List<BlockGatherable> gatherables = Collections.singletonList(gatherable);
            SimpleBlockGatheringSpot spot = new SimpleBlockGatheringSpot(area::boxes, gatherables);
            gatheringService.addBlockGatheringSpot(spot);
        }

        // Human mine
        {
            Location location = new Location(world, -1156, 26, 489);
            BlockOffsets offsets = new BlockOffsets(0, 29, 30, 0, 20, 0);
            BoundingBlockBox area = new BoundingBlockBox(offsets, location, BlockFace.NORTH);

            Set<Material> materials = new HashSet<>();
            materials.add(Material.STONE);
            materials.add(Material.COBBLESTONE);

            BlockGatherable gatherable = blockGatherableFactory.create(
                    materials,
                    Material.BEDROCK,
                    () -> Collections.singletonList(getStoneDrop()),
                    defaultCooldownSupplier);
            List<BlockGatherable> gatherables = Collections.singletonList(gatherable);
            SimpleBlockGatheringSpot spot = new SimpleBlockGatheringSpot(area::boxes, gatherables);
            gatheringService.addBlockGatheringSpot(spot);
        }

        // Human farm
        {
            Location location = new Location(world, -1620, 32, -47);
            BlockOffsets offsets = new BlockOffsets(0, 82, 71, 0, 18, 0);
            BoundingBlockBox area = new BoundingBlockBox(offsets, location, BlockFace.NORTH);

            MaterialData ripeSeeds = new Crops(Material.CROPS, CropState.RIPE);
            MaterialData cooldownMaterialData = new Crops(Material.CROPS, CropState.VERY_SMALL);

            List<BlockGatherable> gatherables = new ArrayList<>();
            gatherables.add(new SimpleBlockGatherable(
                    materialData -> materialData.equals(ripeSeeds),
                    cooldownMaterialData,
                    () -> Collections.singletonList(getWheatDrop()),
                    () -> new Duration(5, Duration.Type.SECONDS)
            ));
            SimpleBlockGatheringSpot spot = new SimpleBlockGatheringSpot(area::boxes, gatherables);
            gatheringService.addBlockGatheringSpot(spot);
        }

        // Orc lumber mill
        {
            Location location = new Location(world, 445, 35, -5);
            BlockOffsets offsets = new BlockOffsets(0, 34, 23, 0, 3, 0);
            BoundingBlockBox area = new BoundingBlockBox(offsets, location, BlockFace.NORTH);

            Set<Material> materials = new HashSet<>();
            materials.add(Material.LOG);
            materials.add(Material.LOG_2);

            BlockGatherable gatherable = blockGatherableFactory.create(
                    materials,
                    Material.BEDROCK,
                    () -> Collections.singletonList(getWoodDrop()),
                    defaultCooldownSupplier);
            List<BlockGatherable> gatherables = Collections.singletonList(gatherable);
            SimpleBlockGatheringSpot spot = new SimpleBlockGatheringSpot(area::boxes, gatherables);
            gatheringService.addBlockGatheringSpot(spot);
        }

        // Orc mine
        {
            Location location = new Location(world, -138, 33, 475);
            BlockOffsets offsets = new BlockOffsets(0, 33, 33, 0, 3, 0);
            BoundingBlockBox area = new BoundingBlockBox(offsets, location, BlockFace.NORTH);

            Set<Material> materials = new HashSet<>();
            materials.add(Material.STONE);
            materials.add(Material.COBBLESTONE);

            BlockGatherable gatherable = blockGatherableFactory.create(
                    materials,
                    Material.BEDROCK,
                    () -> Collections.singletonList(getStoneDrop()),
                    defaultCooldownSupplier);
            List<BlockGatherable> gatherables = Collections.singletonList(gatherable);
            SimpleBlockGatheringSpot spot = new SimpleBlockGatheringSpot(area::boxes, gatherables);
            gatheringService.addBlockGatheringSpot(spot);
        }

        // Orc farm
        {
            Location spawnLocation1 = new Location(world, 89, 43, 420);
            double spawnRadius1 = 4;
            Location spawnLocation2 = new Location(world, 64, 43, 415);
            double spawnRadius2 = 2;
            Location spawnLocation3 = new Location(world, 59, 43, 416);
            double spawnRadius3 = 1;
            List<Supplier<Location>> spawnLocationSuppliers = new ArrayList<>();
            spawnLocationSuppliers.add(createSpawnLocationSupplier(spawnLocation1, spawnRadius1));
            spawnLocationSuppliers.add(createSpawnLocationSupplier(spawnLocation2, spawnRadius2));
            spawnLocationSuppliers.add(createSpawnLocationSupplier(spawnLocation3, spawnRadius3));

            Random random = new Random(System.currentTimeMillis());
            Supplier<Location> spawnLocationSupplier = () -> spawnLocationSuppliers.get(random.nextInt(3)).get();

            List<EntityGatherable> gatherables = new ArrayList<>();
            gatherables.add(new SimpleEntityGatherable(
                    EntityType.PIG,
                    5,
                    spawnLocationSupplier,
                    () -> Collections.singletonList(getPorkDrop()),
                    () -> new Duration(5, Duration.Type.SECONDS)
            ));
            SimpleEntityGatheringSpot spot = new SimpleEntityGatheringSpot(entity -> entity.getType() == EntityType.PIG, gatherables);
            gatheringService.addEntityGatheringSpot(spot);
        }
    }

    private Supplier<Location> createSpawnLocationSupplier(Location location, double radius) {
        return () -> {
            Vector vector = RandomUtils.getRandomVector().multiply(radius);
            vector.setY(0.0D);
            return location.clone().add(vector);
        };
    }

    public static ItemStack getWoodDrop() {
        ItemStack wood = new ItemStack(Material.LOG);
        ItemMeta meta = wood.getItemMeta();
        meta.setDisplayName("Wood");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Resource", "", ChatColor.GRAY + "Used in the construction of siege equipment."));
        wood.setItemMeta(meta);
        return wood;
    }

    public static ItemStack getStoneDrop() {
        ItemStack stone = new ItemStack(Material.STONE);
        ItemMeta meta = stone.getItemMeta();
        meta.setDisplayName("Stone");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Resource", "", ChatColor.GRAY + "Used in the construction of siege equipment."));
        stone.setItemMeta(meta);
        return stone;
    }

    public static ItemStack getWheatDrop() {
        ItemStack wheat = new ItemStack(Material.WHEAT);
        ItemMeta meta = wheat.getItemMeta();
        meta.setDisplayName("Wheat");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Resource", "", ChatColor.GRAY + "Used to create bread."));
        wheat.setItemMeta(meta);
        return wheat;
    }

    public static ItemStack getPorkDrop() {
        ItemStack meat = new ItemStack(Material.PORK);
        ItemMeta meta = meat.getItemMeta();
        meta.setDisplayName("Meat");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Resource", "", ChatColor.GRAY + "Used to create cooked meat."));
        meat.setItemMeta(meta);
        return meat;
    }
}
