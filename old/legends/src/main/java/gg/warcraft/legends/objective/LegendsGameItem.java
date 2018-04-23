package gg.warcraft.legends.objective;

import java.util.Map;

public class LegendsGameItem {
    private final String id;
    private final Map<String, Integer> score;
    private final Map<String, String> objectives;
    private final int secondsToTally;

    public LegendsGameItem(String id, Map<String, Integer> score, Map<String, String> objectives, int secondsToTally) {
        this.id = id;
        this.score = score;
        this.objectives = objectives;
        this.secondsToTally = secondsToTally;
    }

    public String getId() {
        return id;
    }

    public Map<String, Integer> getScore() {
        return score;
    }

    public Map<String, String> getObjectives() {
        return objectives;
    }

    public int getSecondsToTally() {
        return secondsToTally;
    }
}
/*
objectives.put("HUMAN_FARM", "HUMAN");
        objectives.put("HUMAN_KEEP_EAST", "HUMAN");
        objectives.put("HUMAN_KEEP_WEST", "HUMAN");
        objectives.put("HUMAN_LUMBER_MILL", "HUMAN");
        objectives.put("HUMAN_MINE", "HUMAN");
        objectives.put("HUMAN_OUTPOST_ORC", "HUMAN");

        objectives.put("ORC_FARM", "ORC");
        objectives.put("ORC_KEEP_EAST", "ORC");
        objectives.put("ORC_KEEP_WEST", "ORC");
        objectives.put("ORC_LUMBER_MILL", "ORC");
        objectives.put("ORC_MINE", "ORC");
        objectives.put("ORC_OUTPOST_HUMAN", "ORC");
 */