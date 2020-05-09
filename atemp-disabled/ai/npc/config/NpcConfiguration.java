package gg.warcraft.legends.api.ai.npc.config;

import gg.warcraft.legends.api.ai.npc.NpcType;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.world.config.LocationConfig;

public interface NpcConfiguration {

    String getName();

    NpcType getType();

    EntityType getEntityType();

    LocationConfig getLocation();
}
