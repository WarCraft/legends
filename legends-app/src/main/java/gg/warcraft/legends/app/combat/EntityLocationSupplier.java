package gg.warcraft.legends.app.combat;

import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.world.location.Location;

import java.util.UUID;
import java.util.function.Supplier;

public class EntityLocationSupplier implements Supplier<Location> {
    private final EntityQueryService entityQueryService;
    private final UUID entityId;

    private Location previousLocation;

    public EntityLocationSupplier(EntityQueryService entityQueryService, Entity entity) {
        this.entityQueryService = entityQueryService;
        this.entityId = entity.getId();
        this.previousLocation = entity.getLocation();
    }

    @Override
    public Location get() {
        Entity entity = entityQueryService.getEntity(entityId);
        if (entity == null) {
            return previousLocation;
        }
        return entity.getLocation();
    }
}
