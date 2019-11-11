package gg.warcraft.legends.app.combat.warrior.armsmaster;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.legends.app.combat.warrior.effect.WarriorEffectFactory;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.WorldService;

public class Hamstring extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound HIT_SOUND = Sound.ENTITY_SILVERFISH_DEATH;
    private static final Sound MISS_SOUND = Sound.ENTITY_FISHING_BOBBER_THROW;

    private final EntityCommandService entityCommandService;
    private final WorldService WorldService;
    private final WarriorEffectFactory warriorEffectFactory;

    @Inject
    public Hamstring(EntityCommandService entityCommandService, WorldService WorldService,
                     WarriorEffectFactory warriorEffectFactory, @Assisted AbilitySnapshot snapshot,
                     @Assisted("caster") Entity caster, @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.WorldService = WorldService;
        this.warriorEffectFactory = warriorEffectFactory;
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        PotionEffect slow = getSnapshot().getPotionEffect(PotionEffectType.SLOW);

        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.addPotionEffect(entity.getId(), slow);

        warriorEffectFactory.createHamstringEffect(entity, slow.getDuration());
        WorldService.playSound(entity.getLocation(), HIT_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void onMiss(Entity entity) {
        WorldService.playSound(entity.getLocation(), MISS_SOUND, SOUND_CATEGORY, 1, 1);
    }
}
