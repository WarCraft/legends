package gg.warcraft.legends.data;

import gg.warcraft.legends.objective.DefaultLegendsObjectiveService;
import org.bukkit.block.Block;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;
import java.util.UUID;

// TODO refactor into Redis Adapter
public class DataService {
    private static final DataService instance = new DataService();

    private static final String COMMAND_BLOCKS_COLLECTION = "CommandBlocks";

    public static DataService getInstance() {
        return instance;
    }

    private final Jedis jedis;

    private DataService() {
        jedis = new Jedis("localhost");

        // initialize so it can register itself to RunnableUtils
        new LeaderboardPublisher();
    }

    public void addKillingBlow(UUID playerId) {
        String currentWar = DefaultLegendsObjectiveService.CURRENT_WAR;
        jedis.zincrby("KillingBlows" + currentWar, 1, playerId.toString());
    }

    public Set<Tuple> getTopKillingBlows() {
        String currentWar = DefaultLegendsObjectiveService.CURRENT_WAR;
        return jedis.zrevrangeWithScores("KillingBlows" + currentWar, 0, 99);
    }

    public void addKIA(UUID playerId) {
        jedis.sadd("KIA", playerId.toString());
    }

    public boolean wasKIA(UUID playerId, boolean remove) {
        if (playerId == null) {
            return false;
        }
        if (remove) {
            return jedis.srem("KIA", playerId.toString()) == 1; // returns 1 for removed, 0 for wasn't in the set
        } else {
            return jedis.sismember("KIA", playerId.toString());
        }
    }

    private String createCommandBlockKey(Block block) {
        return "x" + block.getX() + "y" + block.getY() + "z" + block.getZ();
    }

    public void addCommandBlock(Block block, String command) {
        String key = createCommandBlockKey(block);
        jedis.hset(COMMAND_BLOCKS_COLLECTION, key, command);
    }

    public String getCommandBlockCommand(Block block) {
        return jedis.hget(COMMAND_BLOCKS_COLLECTION, createCommandBlockKey(block));
    }

    public void removeCommandBlock(Block block) {
        String key = createCommandBlockKey(block);
        jedis.hdel(COMMAND_BLOCKS_COLLECTION, key);
    }
}
