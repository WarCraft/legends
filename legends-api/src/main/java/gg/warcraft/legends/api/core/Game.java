package gg.warcraft.legends.api.core;

import java.util.Map;

public interface Game {

    int getId();

    Map<String, Integer> getScore();

    Map<String, String> getObjectives();

    int getSecondsToNextTally();
}
