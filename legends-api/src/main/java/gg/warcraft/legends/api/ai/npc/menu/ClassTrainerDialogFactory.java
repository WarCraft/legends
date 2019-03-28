package gg.warcraft.legends.api.ai.npc.menu;

import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public interface ClassTrainerDialogFactory {

    Menu createClassTrainerDialog(UUID playerId, String trainerName);
}
