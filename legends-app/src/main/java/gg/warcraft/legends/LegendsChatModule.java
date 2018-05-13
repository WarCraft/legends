package gg.warcraft.legends;

import com.google.inject.AbstractModule;
import gg.warcraft.monolith.app.chat.logger.ConsoleMessageLogger;
import gg.warcraft.monolith.app.chat.logger.MessageLogger;

public class LegendsChatModule extends AbstractModule {

    @Override
    public void configure() {
        bind(MessageLogger.class).to(ConsoleMessageLogger.class);
    }
}
