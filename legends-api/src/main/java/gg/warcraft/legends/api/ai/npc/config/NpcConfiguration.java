package gg.warcraft.legends.api.ai.npc.config;

import gg.warcraft.legends.api.ai.npc.NpcType;
import gg.warcraft.monolith.api.config.LocationConfiguration;
import gg.warcraft.monolith.api.entity.EntityType;

public interface NpcConfiguration {

    String getName();

    NpcType getType();

    EntityType getEntityType();

    LocationConfiguration getLocation();
}
