package gg.warcraft.legends.app.ai.npc.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.ai.npc.NpcType;
import gg.warcraft.legends.api.ai.npc.config.NpcConfiguration;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.world.location.config.LocationConfig;

public class JacksonNpcConfiguration implements NpcConfiguration {
    private final String name;
    private final NpcType type;
    private final EntityType entityType;
    private final LocationConfig location;

    @JsonCreator
    public JacksonNpcConfiguration(@JsonProperty("name") String name,
                                   @JsonProperty("type") NpcType type,
                                   @JsonProperty("entityType") EntityType entityType,
                                   @JsonProperty("location") LocationConfig location) {
        this.name = name;
        this.type = type;
        this.entityType = entityType;
        this.location = location;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NpcType getType() {
        return type;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public LocationConfig getLocation() {
        return location;
    }
}
