package gg.warcraft.legends.app.combat.warrior.status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import java.util.UUID;

public class StandYourGroundStatusEffect implements StatusEffect {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound DAMAGED_SOUND = Sound.ENTITY_IRONGOLEM_HURT;

    private final EntityQueryService entityQueryService;
    private final WorldCommandService worldCommandService;
    private final UUID entityId;
    private final Duration duration;

    @Inject
    public StandYourGroundStatusEffect(EntityQueryService entityQueryService, WorldCommandService worldCommandService,
                                       @Assisted UUID entityId, @Assisted Duration duration) {
        this.entityQueryService = entityQueryService;
        this.worldCommandService = worldCommandService;
        this.entityId = entityId;
        this.duration = duration;
    }

    @Override
    public UUID getEntityId() {
        return entityId;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = entityQueryService.getEntity(entityId);
        worldCommandService.playSound(entity.getLocation(), DAMAGED_SOUND, SOUND_CATEGORY, 1, 1);
    }
}
