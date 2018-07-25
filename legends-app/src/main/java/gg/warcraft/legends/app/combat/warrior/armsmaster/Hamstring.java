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

public class Hamstring extends AbstractAbility implements MeleeAbility {
    private final EntityCommandService entityCommandService;

    @Inject
    public Hamstring(EntityCommandService entityCommandService, @Assisted AbilitySnapshot snapshot,
                     @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
    }

    @Override
    public void onHit(Entity entity) {
        float damage = getSnapshot().getFloat(AbilityConfigurationValue.DAMAGE);
        PotionEffect slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);

        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.addPotionEffect(entity.getId(), slow);

        // new HamstringEffect(target, slow.getDuration());
        // TODO play sound
    }

    @Override
    public void onMiss(Entity entity) {

    }
}
