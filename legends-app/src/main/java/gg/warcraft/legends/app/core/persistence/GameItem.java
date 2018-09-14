package gg.warcraft.legends.app.core.persistence;

import java.util.Map;

public class GameItem {
    private final int id;
    private final String type;
    private final Map<String, Integer> points;
    private final Map<String, String> objectives;
    private final int timer;
    private final String startTime;
    private final String endTime;

    public GameItem(int id, String type, Map<String, Integer> points, Map<String, String> objectives, int timer,
                    String startTime, String endTime) {
        this.id = id;
        this.type = type;
        this.points = points;
        this.objectives = objectives;
        this.timer = timer;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public Map<String, String> getObjectives() {
        return objectives;
    }

    public int getTimer() {
        return timer;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
