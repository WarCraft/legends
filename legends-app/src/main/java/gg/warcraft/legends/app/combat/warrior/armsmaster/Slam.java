package gg.warcraft.legends.app.combat.warrior.armsmaster;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.combat.api.ability.AbstractAbility;
import gg.warcraft.combat.api.ability.MeleeAbility;
import gg.warcraft.combat.api.ability.config.AbilityConfigurationValue;
import gg.warcraft.combat.api.ability.config.AbilitySnapshot;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.combat.value.CombatValue;
import gg.warcraft.monolith.api.entity.Entity;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.math.Vector3f;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.WorldService;

public class Slam extends AbstractAbility implements MeleeAbility {
    private static final SoundCategory SOUND_CATEGORY = SoundCategory.HOSTILE;
    private static final Sound HIT_SOUND = Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR;
    private static final Sound MISS_SOUND = Sound.ENTITY_FISHING_BOBBER_THROW;

    private final EntityCommandService entityCommandService;
    private final WorldService WorldService;

    private final Vector3f direction;

    @Inject
    public Slam(EntityCommandService entityCommandService, WorldService WorldService,
                @Assisted AbilitySnapshot snapshot, @Assisted("caster") Entity caster,
                @Assisted("target") Entity meleeTarget) {
        super(caster.getId(), snapshot);
        this.entityCommandService = entityCommandService;
        this.WorldService = WorldService;
        this.direction = caster.getEyeLocation().rotation();
    }

    @Override
    public void onHit(Entity entity) {
        CombatValue damage = getSnapshot().getValue(AbilityConfigurationValue.DAMAGE);
        float knockback = getSnapshot().getFloat(AbilityConfigurationValue.KNOCKBACK);
        PotionEffect confusion = getSnapshot().getPotionEffect(PotionEffectType.CONFUSION);
        entityCommandService.damage(entity.getId(), damage);
        entityCommandService.knockback(entity.getId(), direction, knockback);
        entityCommandService.addPotionEffect(entity.getId(), confusion);

        WorldService.playSound(entity.getLocation(), HIT_SOUND, SOUND_CATEGORY, 1, 1);
    }

    @Override
    public void onMiss(Entity entity) {
        WorldService.playSound(entity.getLocation(), MISS_SOUND, SOUND_CATEGORY, 1, 1);
    }
}
