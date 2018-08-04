package gg.warcraft.legends.app.combat.ranger.marksman;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.combat.api.ability.service.AbilityCommandService;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import org.joml.Vector3fc;

public class PowerShot extends AbstractAbility implements MeleeAbility {
    private static final float SPEED = 3; // suggested speed of 0.6 by the spigot docs is way too low
    private static final float SPREAD = 2; // suggested spread of 12 by the spigot docs is way too wide

    private final EntityCommandService entityCommandService;
    private final Vector3fc knockbackDirection;

    @Inject
    public PowerShot(AbilityCommandService abilityCommandService, EntityCommandService entityCommandService,
                     @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                     @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.knockbackDirection = caster.getEyeLocation().getDirection();
        abilityCommandService.createArrow(this, caster.getId(), caster.getEyeLocation(),
                caster.getEyeLocation().getDirection(), SPEED, SPREAD);
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        float knockback = getSnapshot().getFloat(AbilityConfigurationValue.KNOCKBACK);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.knockback(entity.getId(), knockbackDirection, knockback);
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}
