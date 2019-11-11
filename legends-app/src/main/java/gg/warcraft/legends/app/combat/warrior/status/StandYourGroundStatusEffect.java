package gg.warcraft.legends.app.combat.warrior.status;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.event.EntityDamageEvent;
import gg.warcraft.monolith.api.entity.service.EntityQueryService;
import gg.warcraft.monolith.api.entity.status.AbstractStatusEffect;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.WorldService;

import java.util.UUID;

public class StandYourGroundStatusEffect extends AbstractStatusEffect {
    private static final String NAME = "Stand Your Ground";
    private static final String TYPE = "legends.armsmaster.standyourground";
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound DAMAGED_SOUND = Sound.ENTITY_IRON_GOLEM_HURT;

    private final EntityQueryService entityQueryService;
    private final WorldService WorldService;

    @Inject
    public StandYourGroundStatusEffect(EntityQueryService entityQueryService, WorldService WorldService,
                                       @Assisted UUID entityId, @Assisted Duration duration) {
        super(entityId, NAME, TYPE, duration);
        this.entityQueryService = entityQueryService;
        this.WorldService = WorldService;
    }

    @Override
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = entityQueryService.getEntity(getEntityId());
        WorldService.playSound(entity.getLocation(), DAMAGED_SOUND, SOUND_CATEGORY, 1, 1);
    }
}
