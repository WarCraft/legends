package gg.warcraft.legends.api.core.game.service;

import gg.warcraft.legends.api.config.ServerConfiguration;

public interface GameCommandService {

    void readConfiguration(ServerConfiguration configuration);

    void initializeActiveGame();

    void tickActiveGame();

    void startWar();

    void runTally();

    void runScore();

    void endWar();

    void startSkirmish();

    void endSkirmish();
}
