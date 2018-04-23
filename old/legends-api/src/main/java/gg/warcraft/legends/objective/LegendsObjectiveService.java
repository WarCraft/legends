package gg.warcraft.legends.objective;

import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.objective.Objective;
import gg.warcraft.monolith.objective.ObjectiveService;

import java.util.List;

public interface LegendsObjectiveService extends ObjectiveService {

    static LegendsObjectiveService getInstance() {
        return (LegendsObjectiveService) Monolith.getInstance().getService(Monolith.Service.OBJECTIVE);
    }

    List<Objective> getObjectives();

    Objective getObjective(String name);

    Scoreboard getScoreboard();
}
