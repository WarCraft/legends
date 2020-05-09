package gg.warcraft.legends.api.ai.npc.service;

import gg.warcraft.legends.api.ai.npc.Npc;

import java.util.Set;
import java.util.UUID;

public interface NpcQueryService {

    Set<UUID> getNpcIds();

    Npc getNpc(UUID entityId);
}
