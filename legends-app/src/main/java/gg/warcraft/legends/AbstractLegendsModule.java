package gg.warcraft.legends;

import com.google.inject.AbstractModule;
import gg.warcraft.monolith.api.data.DataService;
import gg.warcraft.monolith.app.chat.logger.ConsoleMessageLogger;
import gg.warcraft.monolith.app.chat.logger.MessageLogger;
import gg.warcraft.monolith.app.data.MockDataService;

public abstract class AbstractLegendsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataService.class).to(MockDataService.class); // TODO update to JedisDataService on live
        bind(MessageLogger.class).to(ConsoleMessageLogger.class);
    }
}
