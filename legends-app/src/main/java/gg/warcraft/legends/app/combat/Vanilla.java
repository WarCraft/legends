package gg.warcraft.legends.app.combat;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.service.WorldCommandService;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.UUID;

public class Vanilla extends AbstractAbility implements MeleeAbility {
    private final EntityCommandService entityCommandService;
    private final WorldCommandService worldCommandService; // NOTE leaving this here for sounds
    private final Vector3fc direction;

    @Inject
    public Vanilla(EntityCommandService entityCommandService, WorldCommandService worldCommandService,
                   @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                   @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.worldCommandService = worldCommandService;
        this.direction = caster.getEyeLocation().getDirection();
    }

    @Override
    public void onHit(Entity entity) {
        UUID entityId = entity.getId();
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        entityCommandService.damage(entityId, damage);

        // if (!entity.isOnGround()) {
        //    entityCommandService.setVelocity(entityId, direction.mul(0.245f, new Vector3f()).add(0, 0, 0.1f)); // NOTE this used to be setY(0.1f), check if add 0.1f also gives the desired result
        // } else {
        entityCommandService.setVelocity(entityId, direction.mul(0.19f, new Vector3f()));
        // }
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}
