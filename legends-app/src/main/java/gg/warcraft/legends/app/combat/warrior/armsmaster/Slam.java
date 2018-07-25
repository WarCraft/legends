package gg.warcraft.legends.app.combat.warrior.armsmaster;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;
import org.joml.Vector3f;

public class Slam extends AbstractAbility implements MeleeAbility {
    private final EntityCommandService entityCommandService;

    @Inject
    public Slam(EntityCommandService entityCommandService, @Assisted AbilitySnapshot snapshot,
                @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
    }

    @Override
    public void onHit(Entity entity) {
        float damage = getSnapshot().getFloat(AbilityConfigurationValue.DAMAGE);
        Vector3f direction = getCaster().getEyeLocation().getDirection();
        float knockback = getSnapshot().getFloat(AbilityConfigurationValue.KNOCKBACK);
        PotionEffect confusion = getSnapshot().getPotionEffect(PotionEffectType.CONFUSION);

        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.knockback(getCasterId(), direction, knockback);
        entityCommandService.addPotionEffect(getCasterId(), confusion);

        // TODO play sound
    }

    @Override
    public void onMiss(Entity entity) {

    }
}
