package gg.warcraft.legends.app.combat.warrior.armsmaster;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Location;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;

public class StandYourGround extends AbstractAbility implements MeleeAbility {

    @Inject
    public StandYourGround(EntityCommandService entityCommandService, @Assisted AbilitySnapshot snapshot,
                           @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        PotionEffect resistance = getSnapshot().getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        PotionEffect fireResistance = getSnapshot().getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        PotionEffect slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);

        entityCommandService.addPotionEffect(caster.getId(), resistance);
        entityCommandService.addPotionEffect(caster.getId(), fireResistance);
        entityCommandService.addPotionEffect(caster.getId(), slow);


        AttributeInstance kbr = caster.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        kbr.setBaseValue(1);
        RunnableUtils.runLater(() -> kbr.setBaseValue(kbr.getDefaultValue()), slow.getDuration());

        new StandYourGroundEffect(caster, slow.getDuration());

        Location location = caster.getLocation();
        location.getWorld().playSound(location, Sound.ENTITY_IRONGOLEM_DEATH, 1, 1);
    }

    @Override
    public void onHit(Entity entity) {
        // do nothing
    }

    @Override
    public void onMiss(Entity entity) {
        // do nothing
    }
}
