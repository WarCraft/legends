package gg.warcraft.legends.app.core.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.core.service.ServerRepository;

import java.util.Map;

@Singleton
public class DefaultServerRepository implements ServerRepository {
    private int favorPlayerKill;
    private int favorEliteMobKill;
    private int favorMobKill;
    private Map<String, Integer> objectivePoints;
    private Map<String, Integer> objectiveCrates;

    @Override
    public void read(ServerConfiguration configuration) {
        this.favorPlayerKill = configuration.getFavorPlayerKill();
        this.favorEliteMobKill = configuration.getFavorEliteMobKill();
        this.favorMobKill = configuration.getFavorMobKill();
        this.objectivePoints = configuration.getObjectivePoints();
        this.objectiveCrates = configuration.getObjectiveCrates();
    }

    @Override
    public int getFavorPlayerKill() {
        return favorPlayerKill;
    }

    @Override
    public int getFavorEliteMobKill() {
        return favorEliteMobKill;
    }

    @Override
    public int getFavorMobKill() {
        return favorMobKill;
    }

    @Override
    public int getObjectivePoints(String objectiveType) {
        return objectivePoints.getOrDefault(objectiveType, 0);
    }

    @Override
    public int getObjectiveCrates(String objectiveType) {
        return objectiveCrates.getOrDefault(objectiveType, 0);
    }
}
