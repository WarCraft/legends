package gg.warcraft.legends.app;

import com.google.inject.AbstractModule;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerDataRepository;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerCommandService;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerDataRepository;
import gg.warcraft.legends.app.entity.player.service.DefaultLegendsPlayerQueryService;
import gg.warcraft.legends.app.entity.service.DefaultLegendsEntityCommandService;
import gg.warcraft.legends.app.entity.service.DefaultLegendsEntityQueryService;
import gg.warcraft.monolith.api.data.DataService;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerDataRepository;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
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
        configureEntity();
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

    private void configureEntity() {
        bind(EntityCommandService.class).to(DefaultLegendsEntityCommandService.class);
        bind(EntityQueryService.class).to(DefaultLegendsEntityQueryService.class);

        bind(PlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        bind(LegendsPlayerCommandService.class).to(DefaultLegendsPlayerCommandService.class);
        bind(PlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
        bind(LegendsPlayerQueryService.class).to(DefaultLegendsPlayerQueryService.class);
        bind(PlayerDataRepository.class).to(DefaultLegendsPlayerDataRepository.class);
        bind(LegendsPlayerDataRepository.class).to(DefaultLegendsPlayerDataRepository.class);
    }
}
