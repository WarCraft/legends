package gg.warcraft.legends.api.ai.npc.service;

import com.google.common.collect.ImmutableSet;
import gg.warcraft.legends.api.ai.npc.Npc;

import java.util.UUID;

public interface NpcRepository {

    Npc getNpc(UUID npcId);

    ImmutableSet<UUID> getNpcIds();

    void save(Npc npc);

    void delete(UUID npcId);
}
