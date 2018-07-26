package gg.warcraft.legends.app.combat.warrior.armsmaster;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.legends.app.combat.warrior.effect.WarriorEffectFactory;
import gg.warcraft.legends.app.combat.warrior.status.WarriorStatusEffectFactory;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.world.PotionEffect;
import gg.warcraft.monolith.api.world.PotionEffectType;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

public class StandYourGround extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_IRONGOLEM_DEATH;

    @Inject
    public StandYourGround(EntityCommandService entityCommandService, StatusCommandService statusCommandService,
                           WorldCommandService worldCommandService, WarriorEffectFactory warriorEffectFactory,
                           WarriorStatusEffectFactory warriorStatusEffectFactory, @Assisted AbilitySnapshot snapshot,
                           @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        PotionEffect resistance = getSnapshot().getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        PotionEffect fireResistance = getSnapshot().getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        PotionEffect slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);

        entityCommandService.addPotionEffect(caster.getId(), resistance);
        entityCommandService.addPotionEffect(caster.getId(), fireResistance);
        entityCommandService.addPotionEffect(caster.getId(), slow);

        // TODO
//        AttributeInstance kbr = caster.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
//        kbr.setBaseValue(1);
//        RunnableUtils.runLater(() -> kbr.setBaseValue(kbr.getDefaultValue()), slow.getDuration());

        warriorEffectFactory.createStandYourGroundEffect(caster, slow.getDuration());
        worldCommandService.playSound(caster.getLocation(), CAST_SOUND, SOUND_CATEGORY, 1, 1);

        StatusEffect statusEffect =
                warriorStatusEffectFactory.createStandYourGroundStatusEffect(caster.getId(), slow.getDuration());
        statusCommandService.addStatusEffect(statusEffect, caster.getId());
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
