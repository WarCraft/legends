package gg.warcraft.legends.app.combat.warrior.armsmaster;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.legends.app.combat.warrior.effect.WarriorEffectFactory;
import gg.warcraft.legends.app.combat.warrior.status.WarriorStatusEffectFactory;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.attribute.AttributeModifier;
import gg.warcraft.monolith.api.entity.attribute.GenericAttribute;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeCommandService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.service.WorldCommandService;

import javax.annotation.Nullable;

public class StandYourGround extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound CAST_SOUND = Sound.ENTITY_IRONGOLEM_DEATH;

    @Inject
    public StandYourGround(EntityCommandService entityCommandService, AttributeCommandService attributeCommandService,
                           StatusCommandService statusCommandService, WorldCommandService worldCommandService,
                           TaskService taskService, WarriorEffectFactory warriorEffectFactory,
                           WarriorStatusEffectFactory warriorStatusEffectFactory, @Assisted AbilitySnapshot snapshot,
                           @Assisted("caster") Entity caster, @Assisted("target") @Nullable Entity meleeTarget) {
        super(caster.getId(), snapshot);
        PotionEffect resistance = getSnapshot().getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        PotionEffect fireResistance = getSnapshot().getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        PotionEffect slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);

        entityCommandService.addPotionEffect(caster.getId(), resistance);
        entityCommandService.addPotionEffect(caster.getId(), fireResistance);
        entityCommandService.addPotionEffect(caster.getId(), slow);

        AttributeModifier knockbackModifier =
                attributeCommandService.addAttributeModifier(caster.getId(), GenericAttribute.KNOCKBACK_RESISTANCE, 1);
        taskService.runLater(() -> attributeCommandService.removeAttributeModifier(caster.getId(), knockbackModifier),
                slow.getDuration());

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
