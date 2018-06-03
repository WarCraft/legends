package gg.warcraft.legends.app.entity.service;

import gg.warcraft.legends.api.entity.service.LegendsEntityDataRepository;
import gg.warcraft.monolith.api.entity.Faction;
import gg.warcraft.monolith.api.entity.adapter.EntityServerAdapter;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.service.WorldQueryService;
import gg.warcraft.monolith.app.entity.service.AbstractEntityCommandService;

import java.util.UUID;

public class DefaultLegendsEntityCommandService extends AbstractEntityCommandService {
    private final LegendsEntityDataRepository repository;

    public DefaultLegendsEntityCommandService(LegendsEntityDataRepository repository,
                                              EntityQueryService entityQueryService,
                                              EntityServerAdapter entityServerAdapter,
                                              WorldQueryService worldQueryService, TimeUtils timeUtils) {
        super(entityQueryService, entityServerAdapter, worldQueryService, timeUtils);
        this.repository = repository;
    }

    @Override
    public void setFaction(UUID entityId, Faction faction) {
        // TODO impl
    }
}
