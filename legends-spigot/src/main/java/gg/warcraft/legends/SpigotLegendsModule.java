package gg.warcraft.legends;

import com.google.inject.AbstractModule;
import gg.warcraft.monolith.app.chat.logger.ConsoleMessageLogger;
import gg.warcraft.monolith.app.chat.logger.MessageLogger;

public class SpigotLegendsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageLogger.class).to(ConsoleMessageLogger.class);
    }
}
