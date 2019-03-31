package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.config.ServerConfiguration;

public interface ServerRepository {

    void read(ServerConfiguration configuration);

    int getFavorPlayerKill();

    int getFavorEliteMobKill();

    int getFavorMobKill();

    int getObjectivePoints(String objectiveType);
}
