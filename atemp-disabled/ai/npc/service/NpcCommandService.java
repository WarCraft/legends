package gg.warcraft.legends.api.ai.npc.service;

import gg.warcraft.legends.api.ai.npc.config.NpcConfiguration;

import java.util.UUID;

public interface NpcCommandService {

    void readConfiguration(NpcConfiguration configuration);

    void openDialog(UUID npcId, UUID playerId);
}
