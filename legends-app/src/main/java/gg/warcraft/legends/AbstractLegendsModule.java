package gg.warcraft.legends;

import com.google.inject.AbstractModule;
import gg.warcraft.monolith.api.data.DataService;
import gg.warcraft.monolith.app.chat.logger.ConsoleMessageLogger;
import gg.warcraft.monolith.app.chat.logger.MessageLogger;
import gg.warcraft.monolith.app.data.JedisDataService;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public abstract class AbstractLegendsModule extends AbstractModule {
    private static final String REDIS_HOST = "127.0.0.1";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_TIMEOUT = 10;

    @Override
    protected void configure() {
        configureChat();
        configureData();
    }

    private void configureChat() {
        bind(MessageLogger.class).to(ConsoleMessageLogger.class);
    }

    private void configureData() {
        JedisPoolConfig jedisConfiguration = new JedisPoolConfig();
        jedisConfiguration.setBlockWhenExhausted(false);
        jedisConfiguration.setMaxTotal(8);
        JedisPool jedisPool = new JedisPool(jedisConfiguration, REDIS_HOST, REDIS_PORT, REDIS_TIMEOUT);
        bind(JedisPool.class).toInstance(jedisPool);

        bind(DataService.class).to(JedisDataService.class);
    }
}
