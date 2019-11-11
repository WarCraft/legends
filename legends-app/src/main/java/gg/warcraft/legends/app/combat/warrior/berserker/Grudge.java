package gg.warcraft.legends.app.combat.warrior.berserker;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.legends.app.combat.warrior.effect.WarriorEffectFactory;
import gg.warcraft.legends.app.combat.warrior.status.WarriorStatusEffectFactory;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.status.StatusEffect;
import gg.warcraft.monolith.api.entity.status.service.StatusCommandService;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.WorldService;

import java.util.UUID;

public class Grudge extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound HIT_SOUND = Sound.ENTITY_WITHER_SPAWN;
    private static final Sound MISS_SOUND = Sound.ENTITY_FISHING_BOBBER_THROW;

    private final StatusCommandService statusCommandService;
    private final WorldService WorldService;
    private final WarriorEffectFactory warriorEffectFactory;
    private final WarriorStatusEffectFactory warriorStatusEffectFactory;
    private final UUID targetId;

    @Inject
    public Grudge(StatusCommandService statusCommandService, WorldService WorldService,
                  WarriorEffectFactory warriorEffectFactory, WarriorStatusEffectFactory warriorStatusEffectFactory,
                  @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                  @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.statusCommandService = statusCommandService;
        this.WorldService = WorldService;
        this.warriorEffectFactory = warriorEffectFactory;
        this.warriorStatusEffectFactory = warriorStatusEffectFactory;
        this.targetId = meleeTarget.getId();
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue heal = getSnapshot().getValue(AbilityConfigurationValue.HEAL);
        Duration duration = getSnapshot().getDuration();
        StatusEffect grudge =
                warriorStatusEffectFactory.createGrudgeStatusEffect(targetId, getCasterId(), duration, heal);
        statusCommandService.addStatusEffect(grudge, entity.getId());

        warriorEffectFactory.createGrudgeEffect(entity, duration);
        WorldService.playSound(entity.getLocation(), HIT_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void onMiss(Entity entity) {
        WorldService.playSound(entity.getLocation(), MISS_SOUND, SOUND_CATEGORY, 1, 1);
    }
}
