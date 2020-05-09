package gg.warcraft.legends.api.ai.npc.menu;

import gg.warcraft.monolith.api.menu.Menu;

import java.util.UUID;

public interface BlueprintVendorDialogFactory {

    Menu createBlueprintVendorDialog(UUID playerId);
}
