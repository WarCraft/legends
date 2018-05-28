package gg.warcraft.legends.app.gathering;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.event.EventService;
import gg.warcraft.monolith.api.gathering.BlockGatherable;
import gg.warcraft.monolith.api.gathering.BlockGatheringSpot;
import gg.warcraft.monolith.api.gathering.EntityGatherable;
import gg.warcraft.monolith.api.gathering.EntityGatheringSpot;
import gg.warcraft.monolith.api.gathering.factory.GatheringSpotFactory;
import gg.warcraft.monolith.api.gathering.service.GatheringSpotCommandService;
import gg.warcraft.monolith.api.util.BoundingBlockBox;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.ItemType;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.WorldType;
import gg.warcraft.monolith.api.world.block.BlockType;
import gg.warcraft.monolith.api.world.service.WorldQueryService;
import gg.warcraft.monolith.app.gathering.SimpleBlockGatherable;
import gg.warcraft.monolith.app.gathering.SimpleEntityGatherable;
import gg.warcraft.monolith.app.gathering.handler.BlockGatheredEventHandler;
import gg.warcraft.monolith.app.gathering.handler.EntityGatheredEventHandler;
import gg.warcraft.monolith.app.util.JomlBoundingBlockBox;
import gg.warcraft.monolith.app.util.SimpleDuration;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LegendsGatheringConfiguration {
    private static final Supplier<Duration> DEFAULT_COOLDOWN = () -> new SimpleDuration(Duration.Type.SECONDS, 5);
    // Lumber Mill
    private static final Predicate<BlockType> LUMBER_MILL_CONTAINS = type -> BlockType.OAK_WOOD.equals(type)
            || BlockType.ACACIA_WOOD.equals(type);
    private static final BlockType LUMBER_MILL_COOLDOWN_BLOCK = BlockType.BEDROCK;
    private static final Supplier<List<ItemType>> LUMBER_MILL_DROPS = () ->
            Arrays.asList(ItemType.OAK_WOOD, ItemType.ACACIA_WOOD);
    // Mine
    private static final Predicate<BlockType> MINE_CONTAINS = type -> BlockType.STONE.equals(type)
            || BlockType.COBBLESTONE.equals(type);
    private static final BlockType MINE_COOLDOWN_BLOCK = BlockType.BEDROCK;
    private static final Supplier<List<ItemType>> MINE_DROPS = () ->
            Arrays.asList(ItemType.STONE, ItemType.COBBLESTONE);
    // Wheat Farm
    private static final Predicate<BlockType> WHEAT_FARM_CONTAINS = BlockType.WHEAT_CROPS::equals;
    private static final BlockType WHEAT_FARM_COOLDOWN_BLOCK = BlockType.BEDROCK;
    private static final Supplier<List<ItemType>> WHEAT_FARM_DROPS =() ->
            Collections.singletonList(ItemType.WHEAT_CROPS);
    // Pig Farm
    private static final EntityType PIG_FARM_ENTITY = EntityType.PIG;
    private static final Supplier<List<ItemType>> PIG_FARM_DROPS = () ->
            Collections.singletonList(ItemType.RAW_PORKCHOP);


    private final GatheringSpotCommandService gatheringSpotCommandService;
    private final GatheringSpotFactory gatheringSpotFactory;
    private final WorldQueryService worldQueryService;
    private final EntityQueryService entityQueryService;

    @Inject
    public LegendsGatheringConfiguration(GatheringSpotCommandService gatheringSpotCommandService,
                                         GatheringSpotFactory gatheringSpotFactory,
                                         WorldQueryService worldQueryService,
                                         EntityQueryService entityQueryService,
                                         BlockGatheredEventHandler blockGatheredEventHandler,
                                         EntityGatheredEventHandler entityGatheredEventHandler,
                                         EventService eventService) {
        this.gatheringSpotCommandService = gatheringSpotCommandService;
        this.gatheringSpotFactory = gatheringSpotFactory;
        this.worldQueryService = worldQueryService;
        this.entityQueryService = entityQueryService;

        // Human Lumber Mill
        Vector3fc humanLumberMillMinimumCorner = new Vector3f(-780, 33, 397);
        Vector3fc humanLumberMillMaximumCorner = new Vector3f(-760, 34, 421);
        createLumberMill(humanLumberMillMinimumCorner, humanLumberMillMaximumCorner);
        // Human Mine
        Vector3fc humanMineMinimumCorner = new Vector3f(-1156, 26, 489);
        Vector3fc humanMineMaximumCorner = new Vector3f(-1127, 46, 519);
        createMine(humanMineMinimumCorner, humanMineMaximumCorner);
        // Human Farm
        Vector3fc humanFarmMinimumCorner = new Vector3f(-1620, 32, -47);
        Vector3fc humanFarmMaximumCorner = new Vector3f(-1538, 50, 24);
        createWheatFarm(humanFarmMinimumCorner, humanFarmMaximumCorner);
        // Orc Lumber Mill
        Vector3fc orcLumberMillMinimumCorner = new Vector3f(445, 35, -5);
        Vector3fc orcLumberMillMaximumCorner = new Vector3f(479, 38, 18);
        createLumberMill(orcLumberMillMinimumCorner, orcLumberMillMaximumCorner);
        // Orc Mine
        Vector3fc orcMineMinimumCorner = new Vector3f(-138, 33, 475);
        Vector3fc orcMineMaximumCorner = new Vector3f(-105, 36, 508);
        createMine(orcMineMinimumCorner, orcMineMaximumCorner);
        // Orc Farm
        List<Location> orcFarmSpawnLocations = Arrays.asList(
                worldQueryService.getLocation(WorldType.OVERWORLD, 89, 43, 420),
                worldQueryService.getLocation(WorldType.OVERWORLD, 64, 43, 415),
                worldQueryService.getLocation(WorldType.OVERWORLD, 59, 43, 416));
        createPigFarm(orcFarmSpawnLocations);

        eventService.subscribe(blockGatheredEventHandler);
        eventService.subscribe(entityGatheredEventHandler);
    }

    private void createLumberMill(Vector3fc minimumCorner, Vector3fc maximumCorner) {
        // Set up and creation of the lumber mill
        BoundingBlockBox boundingBlockBox = new JomlBoundingBlockBox(minimumCorner, maximumCorner);
        BlockGatherable blockGatherable = new SimpleBlockGatherable(LUMBER_MILL_CONTAINS, LUMBER_MILL_COOLDOWN_BLOCK,
                LUMBER_MILL_DROPS, DEFAULT_COOLDOWN);
        BlockGatheringSpot humanLumberMill = gatheringSpotFactory.createBlockGatheringSpot(boundingBlockBox,
                Collections.singletonList(blockGatherable));
        gatheringSpotCommandService.addBlockGatheringSpot(humanLumberMill);
    }

    private void createMine(Vector3fc minimumCorner, Vector3fc maximumCorner) {
        // Set up and creation of the mine
        BoundingBlockBox boundingBlockBox = new JomlBoundingBlockBox(minimumCorner, maximumCorner);
        BlockGatherable blockGatherable = new SimpleBlockGatherable(MINE_CONTAINS, MINE_COOLDOWN_BLOCK, MINE_DROPS,
                DEFAULT_COOLDOWN);
        BlockGatheringSpot humanMine = gatheringSpotFactory.createBlockGatheringSpot(boundingBlockBox,
                Collections.singletonList(blockGatherable));
        gatheringSpotCommandService.addBlockGatheringSpot(humanMine);
    }

    private void createWheatFarm(Vector3fc minimumCorner, Vector3fc maximumCorner) {
        // Set up and creation of the human farm
        BoundingBlockBox boundingBlockBox = new JomlBoundingBlockBox(minimumCorner, maximumCorner);
        BlockGatherable blockGatherable = new SimpleBlockGatherable(WHEAT_FARM_CONTAINS, WHEAT_FARM_COOLDOWN_BLOCK,
                WHEAT_FARM_DROPS, DEFAULT_COOLDOWN);
        BlockGatheringSpot humanFarm = gatheringSpotFactory.createBlockGatheringSpot(boundingBlockBox,
                Collections.singletonList(blockGatherable));
        gatheringSpotCommandService.addBlockGatheringSpot(humanFarm);
    }

    private void createPigFarm(List<Location> spawnLocations) {
        // Set up and creation of the orc farm
        Random random = new Random(System.currentTimeMillis());
        Supplier<Location> spawnLocationSupplier = () -> spawnLocations.get(random.nextInt(3));
        EntityGatherable entityGatherable = new SimpleEntityGatherable(PIG_FARM_ENTITY, 5, spawnLocationSupplier,
                PIG_FARM_DROPS, DEFAULT_COOLDOWN);
        EntityGatheringSpot pigFarm = gatheringSpotFactory.createEntityGatheringSpot(
                id -> entityQueryService.getEntity(id) != null, Collections.singletonList(entityGatherable));
        gatheringSpotCommandService.addEntityGatheringSpot(pigFarm);
    }
}
